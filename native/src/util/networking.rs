use bytes::{Buf, BufMut, Bytes, BytesMut};

/// Minecraft doesn't use set-length integers for the MC protocol version
/// This writes a value to a variable integer
pub fn write_varint(buf: &mut BytesMut, mut value: i32) {
    loop {
        let mut temp = (value & 0x7F) as u8;
        value >>= 7;
        if value != 0 {
            temp |= 0x80;
            buf.put_u8(temp);
        } else {
            buf.put_u8(temp);
            break;
        }
    }
}

/// Minecraft doesn't use set-length integers for the MC protocol version
/// This reads a variable integer into a 32-bit integer
pub fn read_varint(buf: &mut BytesMut) -> i32 {
    let mut num_read = 0;
    let mut result = 0;
    loop {
        let read = buf.get_u8();
        let value = (read & 0x7F) as i32;
        result |= value << (7 * num_read);
        if (read & 0x80) == 0 {
            break;
        }
        num_read += 1;
    }
    result
}

pub struct Property {
    pub name: Bytes,
    pub value: Bytes,
    pub signature: Option<Bytes>,
}

pub struct GameProfile {
    pub uuid: u128,
    pub username: Bytes,
    pub properties: Vec<Property>,
}

impl GameProfile {
    pub fn decode(buf: &mut BytesMut) -> anyhow::Result<Self> {
        let uuid = buf.get_u128();
        let username = read_fixed_string(buf, 16)?;
        let props_len = read_varint(buf) as usize;
        let mut properties = Vec::with_capacity(props_len);

        for _ in 0..props_len {
            let name = read_fixed_string(buf, 64)?;
            let value = read_fixed_string(buf, 32767)?;
            let has_signature = buf.get_u8() != 0;
            let signature = if has_signature {
                Some(read_fixed_string(buf, 1024)?)
            } else {
                None
            };

            properties.push(Property { name, value, signature });
        }

        Ok(Self { uuid, username, properties })
    }

    pub fn encode(&self, buf: &mut BytesMut) -> anyhow::Result<()> {
        buf.put_u128(self.uuid);
        write_fixed_string(buf, &self.username,16);
        write_varint(buf, self.properties.len() as i32);
        for prop in &self.properties {
            write_fixed_string(buf, &prop.name, 64);
            write_fixed_string(buf, &prop.value, 32767);
            match &prop.signature {
                Some(sig) => {
                    buf.put_u8(1);
                    write_fixed_string(buf, sig, 1024);
                }
                None => {
                    buf.put_u8(0);
                }
            }
        }
        Ok(())
    }
}

pub struct Identifier {
    pub path: Bytes,
}

impl Identifier {
    pub fn decode(buf: &mut BytesMut) -> anyhow::Result<Self> {
        let path = read_fixed_string(buf, 32767)?;
        Ok(Self {path})
    }

    pub fn encode(&self, buf: &mut BytesMut) -> anyhow::Result<()> {
        write_fixed_string(buf, &self.path, 32767);
        Ok(())
    }
}

pub struct RegistryEntry {
    pub entry_id: Identifier,
    pub data: Option<Bytes>,
}

pub struct Tag {
    pub name: Identifier,
    pub entries: Vec<i32>,
}

pub struct Link {
    pub left_right: u8,
    pub label_varint: Option<i32>,
    pub label_text: Option<Bytes>,
    pub url: Bytes,
}

impl Link {
    pub fn decode(buf: &mut BytesMut) -> anyhow::Result<Self> {
        let left_right = buf.get_u8();
        let label_varint;
        let label_text;
        if left_right == 0 {
            label_varint = Some(read_varint(buf));
            label_text = None;
        } else {
            label_varint = None;
            label_text = Some(read_string(buf)?);
        }
        let url = read_string(buf)?;
        Ok(Self { left_right, label_varint, label_text, url })
    }

    pub fn encode(&self, buf: &mut BytesMut) -> anyhow::Result<()> {
        buf.put_u8(self.left_right);
        if let Some(label) = &self.label_varint {
            write_varint(buf, *label);
        } else if let Some(label_text) = &self.label_text {
            write_string(buf, label_text);
        }
        write_string(buf, &self.url);
        Ok(())
    }
}

pub struct Pack {
    pub namespace: Bytes,
    pub id: Bytes,
    pub version: Bytes,
}

impl Pack {
    pub fn decode(buf: &mut BytesMut) -> anyhow::Result<Self> {
        let namespace = read_fixed_string(buf, 32767)?;
        let id = read_fixed_string(buf, 32767)?;
        let version = read_fixed_string(buf, 32767)?;
        Ok(Self { namespace, id, version })
    }

    pub fn encode(&self, buf: &mut BytesMut) -> anyhow::Result<()> {
        write_fixed_string(buf, &self.namespace, 32767);
        write_fixed_string(buf, &self.id, 32767);
        write_fixed_string(buf, &self.version, 32767);
        Ok(())
    }
}

pub struct Details {
    pub title: Bytes,
    pub description: Bytes,
}

impl Details {
    pub fn decode(buf: &mut BytesMut) -> anyhow::Result<Self> {
        let title = read_fixed_string(buf, 128)?;
        let description = read_fixed_string(buf, 4096)?;
        Ok(Self { title, description })
    }

    pub fn encode(&self, buf: &mut BytesMut) -> anyhow::Result<()> {
        write_fixed_string(buf, &self.title, 128);
        write_fixed_string(buf, &self.description, 4096);
        Ok(())
    }
}

pub fn read_string(buf: &mut BytesMut) -> anyhow::Result<Bytes> {
    let len = read_varint(buf) as usize;

    Ok(buf.split_to(len).freeze())
}

pub fn read_fixed_string(buf: &mut BytesMut, expected_len: i32) -> anyhow::Result<Bytes> {
    buf.advance(4);
    Ok(buf.split_to(expected_len as usize).freeze())
}

pub fn write_string(buf: &mut BytesMut, data: &[u8]) {
    write_varint(buf, data.len() as i32);
    buf.put_slice(data);
}

pub fn write_fixed_string(buf: &mut BytesMut, data: &[u8], expected_len: i32) {
    let slice = data.as_ref();
    buf.put_i32(expected_len);
    buf.put_slice(slice);
}