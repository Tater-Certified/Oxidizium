use std::io::BufRead;
use bytes::{Buf, BufMut, Bytes, BytesMut};
use anyhow::{ensure, Result};
use crate::util::networking::{read_fixed_string, read_string, read_varint, write_fixed_string, write_string, write_varint, Details, GameProfile, Identifier, Link, Pack, RegistryEntry, Tag};

pub trait Packet: Sized {
    fn encode(&self, buf: &mut BytesMut) -> Result<()>;
    fn decode(buf: &mut BytesMut) -> Result<Self>;
    fn packet_id(&self) -> i32;
}


/*
 * Handshaking
*/
pub struct ServerHandshake {
    pub protocol_version: i32,
    pub server_address: Bytes,
    pub server_port: u16,
    pub intent: i32,
}

impl Packet for ServerHandshake {
    fn encode(&self, buf: &mut BytesMut) -> Result<()> {
        write_varint(buf, self.protocol_version);
        write_string(buf, &self.server_address);
        buf.put_u16(self.server_port);
        write_varint(buf, self.intent);
        Ok(())
    }

    fn decode(buf: &mut BytesMut) -> Result<Self> {
        let protocol_version = read_varint(buf);
        let server_address = read_string(buf)?;
        let server_port = buf.get_u16();
        let intent = read_varint(buf);
        Ok(Self{protocol_version, server_address, server_port, intent})
    }

    fn packet_id(&self) -> i32 {
        0x00
    }
}


/*
 * Status
*/
pub struct StatusResponse {
    pub json_response: Bytes,
}

impl Packet for StatusResponse {
    fn encode(&self, buf: &mut BytesMut) -> Result<()> {
        buf.put_slice(&self.json_response);
        Ok(())
    }

    fn decode(buf: &mut BytesMut) -> Result<Self> {
        let json_response = buf.split().freeze();
        Ok(StatusResponse { json_response })
    }

    fn packet_id(&self) -> i32 {
        0x00
    }
}

pub struct PongResponse {
    pub timestamp: i64,
}

impl Packet for PongResponse {
    fn encode(&self, buf: &mut BytesMut) -> Result<()> {
        buf.put_i64(self.timestamp);
        Ok(())
    }

    fn decode(buf: &mut BytesMut) -> Result<Self> {
        let timestamp = buf.get_i64();
        Ok(PongResponse { timestamp })
    }

    fn packet_id(&self) -> i32 {
        0x01
    }
}

pub struct StatusRequest {}

impl Packet for StatusRequest {
    fn encode(&self, _buf: &mut BytesMut) -> Result<()> {
        Ok(())
    }

    fn decode(_buf: &mut BytesMut) -> Result<Self> {
        Ok(StatusRequest {})
    }

    fn packet_id(&self) -> i32 {
        0x00
    }
}

pub struct PingRequest {
    pub timestamp: i64,
}

impl Packet for PingRequest {
    fn encode(&self, buf: &mut BytesMut) -> Result<()> {
        buf.put_i64(self.timestamp);
        Ok(())
    }

    fn decode(buf: &mut BytesMut) -> Result<Self> {
        let timestamp = buf.get_i64();
        Ok(PingRequest { timestamp })
    }

    fn packet_id(&self) -> i32 {
        0x01
    }
}

/*
 * Login
*/
pub struct DisconnectLogin {
    pub reason: Bytes,
}

impl Packet for DisconnectLogin {
    fn encode(&self, buf: &mut BytesMut) -> Result<()> {
        buf.put_slice(&self.reason);
        Ok(())
    }

    fn decode(buf: &mut BytesMut) -> Result<Self> {
        let reason = buf.split().freeze();
        Ok(DisconnectLogin { reason })
    }

    fn packet_id(&self) -> i32 {
        0x00
    }
}

pub struct EncryptionRequest {
    pub server_id: Bytes,
    pub public_key_size: i32,
    pub public_key: Bytes,
    pub verify_token_size: i32,
    pub verify_token: Bytes,
    pub should_auth: bool,
}

impl Packet for EncryptionRequest {
    fn encode(&self, buf: &mut BytesMut) -> Result<()> {
        write_fixed_string(buf, &self.server_id, 20);
        write_varint(buf, self.public_key_size);
        write_string(buf, &self.public_key);
        write_varint(buf, self.verify_token_size);
        write_string(buf, &self.verify_token);
        buf.put_u8(self.should_auth as u8);
        Ok(())
    }

    fn decode(buf: &mut BytesMut) -> Result<Self> {
        let server_id = read_fixed_string(buf, 20)?;
        let public_key_size = read_varint(buf);
        let public_key = read_string(buf)?;
        let verify_token_size = read_varint(buf);
        let verify_token = read_string(buf)?;

        let should_auth = buf.get_u8() == 1;
        Ok(Self {server_id, public_key_size, public_key, verify_token_size, verify_token, should_auth})
    }

    fn packet_id(&self) -> i32 {
        0x01
    }
}

pub struct LoginSuccess {
    pub profile: GameProfile,
}

impl Packet for LoginSuccess {
    fn encode(&self, buf: &mut BytesMut) -> Result<()> {
        self.profile.encode(buf)?;
        Ok(())
    }

    fn decode(buf: &mut BytesMut) -> Result<Self> {
        let profile = GameProfile::decode(buf)?;
        Ok(Self { profile })
    }

    fn packet_id(&self) -> i32 {
        0x02
    }
}

pub struct SetCompression {
    pub threshold: i32,
}

impl Packet for SetCompression {
    fn encode(&self, buf: &mut BytesMut) -> Result<()> {
        write_varint(buf, self.threshold);
        Ok(())
    }

    fn decode(buf: &mut BytesMut) -> Result<Self> {
        let threshold = read_varint(buf);
        Ok(Self { threshold })
    }

    fn packet_id(&self) -> i32 {
        0x03
    }
}

pub struct LoginPluginRequest {
    pub message_id: i32,
    pub channel: Identifier,
    pub data: Bytes,
}

impl Packet for LoginPluginRequest {
    fn encode(&self, buf: &mut BytesMut) -> Result<()> {
        write_varint(buf, self.message_id);
        self.channel.encode(buf)?;
        buf.put_slice(&self.data);
        Ok(())
    }

    fn decode(buf: &mut BytesMut) -> Result<Self> {
        let message_id = read_varint(buf);
        let channel = Identifier::decode(buf)?;
        let data = buf.split().freeze();
        Ok(Self {message_id, channel, data})
    }

    fn packet_id(&self) -> i32 {
        0x04
    }
}

pub struct CookieRequest {
    pub key: Identifier,
}

impl Packet for CookieRequest {
    fn encode(&self, buf: &mut BytesMut) -> Result<()> {
        self.key.encode(buf)?;
        Ok(())
    }

    fn decode(buf: &mut BytesMut) -> Result<Self> {
        let key = Identifier::decode(buf)?;
        Ok(Self { key })
    }

    fn packet_id(&self) -> i32 {
        0x05
    }
}

pub struct LoginStart {
    pub name: Bytes,
    pub player_uuid: u128,
}

impl Packet for LoginStart {
    fn encode(&self, buf: &mut BytesMut) -> Result<()> {
        write_fixed_string(buf, &self.name, 16);
        buf.put_u128(self.player_uuid);
        Ok(())
    }

    fn decode(buf: &mut BytesMut) -> Result<Self> {
        let name = read_fixed_string(buf, 16)?;
        let player_uuid = buf.get_u128();
        Ok(Self { name, player_uuid })
    }

    fn packet_id(&self) -> i32 {
        0x00
    }
}

pub struct EncryptionResponse {
    pub shared_secret_size: i32,
    pub shared_secret: Bytes,
    pub verify_token_size: i32,
    pub verify_token: Bytes,
}

impl Packet for EncryptionResponse {
    fn encode(&self, buf: &mut BytesMut) -> Result<()> {
        write_varint(buf, self.shared_secret_size);
        write_string(buf, &self.shared_secret);
        write_varint(buf, self.verify_token_size);
        write_string(buf, &self.verify_token);
        Ok(())
    }

    fn decode(buf: &mut BytesMut) -> Result<Self> {
        let shared_secret_size = read_varint(buf);
        let shared_secret = read_string(buf)?;
        let verify_token_size = read_varint(buf);
        let verify_token = read_string(buf)?;
        Ok(Self {shared_secret_size, shared_secret, verify_token_size, verify_token})
    }

    fn packet_id(&self) -> i32 {
        0x01
    }
}

pub struct LoginPluginResponse {
    pub message_id: i32,
    pub data: Option<Bytes>,
}

impl Packet for LoginPluginResponse {
    fn encode(&self, buf: &mut BytesMut) -> Result<()> {
        write_varint(buf, self.message_id);

        if let Some(ref data) = self.data {
            buf.put_u8(1);
            buf.put_slice(data);
        } else {
            buf.put_u8(0);
        }

        Ok(())
    }

    fn decode(buf: &mut BytesMut) -> Result<Self> {
        let message_id = read_varint(buf);

        let has_data = buf.get_u8() == 1;
        let data = if has_data {
            Some(buf.split().freeze())
        } else {
            None
        };

        Ok(Self { message_id, data })
    }

    fn packet_id(&self) -> i32 {
        0x02
    }
}

pub struct LoginAcknowledged{}

impl Packet for LoginAcknowledged{
    fn encode(&self, _buf: &mut BytesMut) -> Result<()> {
        Ok(())
    }

    fn decode(_buf: &mut BytesMut) -> Result<Self> {
        Ok(Self{})
    }

    fn packet_id(&self) -> i32 {
        0x03
    }
}

pub struct CookieResponseLogin {
    pub key: Identifier,
    pub payload_size: i32,
    pub payload: Option<Bytes>,
}

impl Packet for CookieResponseLogin {
    fn encode(&self, buf: &mut BytesMut) -> Result<()> {
        self.key.encode(buf)?;

        if let Some (payload) = &self.payload {
            buf.put_u8(1);
            write_varint(buf, self.payload_size);
            buf.put_slice(payload);
        } else {
            buf.put_u8(0);
        }
        Ok(())
    }

    fn decode(buf: &mut BytesMut) -> Result<Self> {
        let key = Identifier::decode(buf)?;
        let has_payload = buf.get_u8() == 1;
        let payload_size;
        let payload = if has_payload {
            payload_size = read_varint(buf);
            Some(buf.split().freeze())
        } else {
            payload_size = 0;
            None
        };
        Ok(Self { key, payload_size, payload })
    }

    fn packet_id(&self) -> i32 {
        0x04
    }
}

/*
 * Configuration
*/
pub struct CookieRequestConfig {
    pub key: Identifier,
}

impl Packet for CookieRequestConfig {
    fn encode(&self, buf: &mut BytesMut) -> Result<()> {
        self.key.encode(buf)?;
        Ok(())
    }

    fn decode(buf: &mut BytesMut) -> Result<Self> {
        let key = Identifier::decode(buf)?;
        Ok(Self { key })
    }

    fn packet_id(&self) -> i32 {
        0x00
    }
}

pub struct ClientboundPluginMessage {
    pub channel: Identifier,
    pub data: Bytes,
}

impl Packet for ClientboundPluginMessage {
    fn encode(&self, buf: &mut BytesMut) -> Result<()> {
        self.channel.encode(buf)?;
        buf.put_slice(&self.data);
        Ok(())
    }

    fn decode(buf: &mut BytesMut) -> Result<Self> {
        let channel = Identifier::decode(buf)?;
        let data = buf.split().freeze();
        Ok(Self { channel, data })
    }

    fn packet_id(&self) -> i32 {
        0x01
    }
}

pub struct DisconnectConfig {
    pub reason: Bytes,
}

impl Packet for DisconnectConfig {
    fn encode(&self, buf: &mut BytesMut) -> Result<()> {
        buf.put_slice(&self.reason);
        Ok(())
    }

    fn decode(buf: &mut BytesMut) -> Result<Self> {
        let reason = buf.split().freeze();
        Ok(Self { reason })
    }

    fn packet_id(&self) -> i32 {
        0x02
    }
}

pub struct FinishConfig {}

impl Packet for FinishConfig {
    fn encode(&self, _buf: &mut BytesMut) -> Result<()> {
        Ok(())
    }

    fn decode(_buf: &mut BytesMut) -> Result<Self> {
        Ok(Self{})
    }

    fn packet_id(&self) -> i32 {
        0x03
    }
}

pub struct ClientboundKeepAlive {
    pub keep_alive_id: i64,
}

impl Packet for ClientboundKeepAlive {
    fn encode(&self, buf: &mut BytesMut) -> Result<()> {
        buf.put_i64(self.keep_alive_id);
        Ok(())
    }

    fn decode(buf: &mut BytesMut) -> Result<Self> {
        let keep_alive_id = buf.get_i64();
        Ok(Self { keep_alive_id })
    }

    fn packet_id(&self) -> i32 {
        0x04
    }
}

pub struct Ping {
    pub id: i32,
}

impl Packet for Ping {
    fn encode(&self, buf: &mut BytesMut) -> Result<()> {
        buf.put_i32(self.id);
        Ok(())
    }

    fn decode(buf: &mut BytesMut) -> Result<Self> {
        let id = buf.get_i32();
        Ok(Self { id })
    }

    fn packet_id(&self) -> i32 {
        0x05
    }
}

pub struct ResetChat {}

impl Packet for ResetChat {
    fn encode(&self, _buf: &mut BytesMut) -> Result<()> {
        Ok(())
    }

    fn decode(_buf: &mut BytesMut) -> Result<Self> {
        Ok(Self{})
    }

    fn packet_id(&self) -> i32 {
        0x06
    }
}

pub struct RegistryData {
    pub registry_id: Identifier,
    pub entry_count: i32,
    pub entries: Vec<RegistryEntry>,
}

impl Packet for RegistryData {
    fn encode(&self, buf: &mut BytesMut) -> Result<()> {
        self.registry_id.encode(buf)?;
        write_varint(buf, self.entry_count);
        for entry in &self.entries {
            entry.entry_id.encode(buf)?;

            match &entry.data {
                Some(data) => {
                    buf.put_u8(1);
                    write_varint(buf, data.len() as i32);
                    buf.put_slice(data);
                }
                None => {
                    buf.put_u8(0);
                }
            }
        }
        Ok(())
    }

    fn decode(buf: &mut BytesMut) -> Result<Self> {
        let registry_id = Identifier::decode(buf)?;
        let entry_count = read_varint(buf);
        let mut entries = Vec::with_capacity(entry_count as usize);

        for _ in 0..entry_count {
            let entry_id = Identifier::decode(buf)?;

            let has_data = buf.get_u8() != 0;
            let data = if has_data {
                let data_len = read_varint(buf) as usize;
                ensure!(buf.remaining() >= data_len, "Incomplete NBT data");
                Some(buf.split_to(data_len).freeze())
            } else {
                None
            };

            entries.push(RegistryEntry { entry_id, data });
        }

        Ok(Self { registry_id, entry_count, entries })
    }

    fn packet_id(&self) -> i32 {
        0x07
    }
}

pub struct RemoveResourcePack {
    pub uuid: Option<u128>,
}

impl Packet for RemoveResourcePack {
    fn encode(&self, buf: &mut BytesMut) -> Result<()> {
        match self.uuid {
            Some(uuid) => {
                buf.put_u8(1);
                buf.put_u128(uuid);
            }
            None => {
                buf.put_u8(0);
            }
        }
        Ok(())
    }

    fn decode(buf: &mut BytesMut) -> Result<Self> {
        let has_uuid = buf.get_u8() != 0;
        let uuid = if has_uuid {
            Some(buf.get_u128())
        } else {
            None
        };

        Ok(Self { uuid })
    }

    fn packet_id(&self) -> i32 {
        0x08
    }
}

pub struct AddResourcePack {
    pub uuid: u128,
    pub url: Bytes,
    pub hash: Bytes,
    pub forced: bool,
    pub prompt_message: Option<Bytes>,
}

impl Packet for AddResourcePack {
    fn encode(&self, buf: &mut BytesMut) -> Result<()> {
        buf.put_u128(self.uuid);
        write_fixed_string(buf, &self.url, 32767);
        write_fixed_string(buf, &self.hash, 40);
        buf.put_u8(self.forced as u8);
        if let Some(prompt_message) = &self.prompt_message {
            buf.put_u8(1);
            buf.split().put_slice(prompt_message);
        } else {
            buf.put_u8(0);
        }
        Ok(())
    }

    fn decode(buf: &mut BytesMut) -> Result<Self> {
        let uuid = buf.get_u128();
        let url = read_fixed_string(buf, 32767)?;
        let hash = read_fixed_string(buf, 40)?;
        let forced = buf.get_u8() != 0;

        let has_message = buf.get_u8() != 0;
        let prompt_message = if has_message {
            Some(buf.split().freeze())
        } else {
            None
        };
        Ok(Self { uuid, url, hash, forced, prompt_message })
    }

    fn packet_id(&self) -> i32 {
        0x09
    }
}

pub struct StoreCookie {
    pub key: Identifier,
    pub payload_size: i32,
    pub payload: Bytes,
}

impl Packet for StoreCookie {
    fn encode(&self, buf: &mut BytesMut) -> Result<()> {
        self.key.encode(buf)?;
        write_varint(buf, self.payload_size);
        buf.put_slice(&self.payload);
        Ok(())
    }

    fn decode(buf: &mut BytesMut) -> Result<Self> {
        let key = Identifier::decode(buf)?;
        let payload_size = read_varint(buf);
        let payload = buf.split().freeze();
        Ok(Self { key, payload_size, payload })
    }

    fn packet_id(&self) -> i32 {
        0x0A
    }
}

pub struct Transfer {
    pub host: Bytes,
    pub port: i32,
}

impl Packet for Transfer {
    fn encode(&self, buf: &mut BytesMut) -> Result<()> {
        write_fixed_string(buf, &self.host, 32767);
        write_varint(buf, self.port);
        Ok(())
    }

    fn decode(buf: &mut BytesMut) -> Result<Self> {
        let host = read_fixed_string(buf, 32767)?;
        let port = read_varint(buf);
        Ok(Self { host, port })
    }

    fn packet_id(&self) -> i32 {
        0x0B
    }
}

pub struct FeatureFlags {
    pub feature_flags_count: i32,
    pub feature_flags: Vec<Identifier>,
}

impl Packet for FeatureFlags {
    fn encode(&self, buf: &mut BytesMut) -> Result<()> {
        write_varint(buf, self.feature_flags_count);
        for flag in &self.feature_flags {
            flag.encode(buf)?;
        }
        Ok(())
    }

    fn decode(buf: &mut BytesMut) -> Result<Self> {
        let feature_flags_count = read_varint(buf);
        let mut feature_flags = Vec::with_capacity(feature_flags_count as usize);
        for _ in 0..feature_flags_count {
            feature_flags.push(Identifier::decode(buf)?);
        }
        Ok(Self {feature_flags_count, feature_flags})
    }

    fn packet_id(&self) -> i32 {
        0x0C
    }
}

pub struct UpdateTags {
    pub registry: Identifier,
    pub tags_size: i32,
    pub tags: Vec<Tag>,
}

impl Packet for UpdateTags {
    fn encode(&self, buf: &mut BytesMut) -> Result<()> {
        self.registry.encode(buf)?;
        write_varint(buf, self.tags_size);

        for tag in &self.tags {
            tag.name.encode(buf)?;
            write_varint(buf, tag.entries.len() as i32);
            for &id in &tag.entries {
                write_varint(buf, id);
            }
        }
        Ok(())
    }

    fn decode(buf: &mut BytesMut) -> Result<Self> {
        let registry = Identifier::decode(buf)?;

        let tags_size = read_varint(buf);
        let mut tags = Vec::with_capacity(tags_size as usize);

        for _ in 0..tags_size {
            let name = Identifier::decode(buf)?;
            let entries_len = read_varint(buf) as usize;
            let mut entries = Vec::with_capacity(entries_len);
            for _ in 0..entries_len {
                entries.push(read_varint(buf));
            }

            tags.push(Tag { name, entries });
        }

        Ok(Self { registry, tags_size, tags})
    }

    fn packet_id(&self) -> i32 {
        0x0D
    }
}

pub struct ClientboundKnownPacks {
    pub known_packs_size: i32,
    pub known_packs: Vec<Pack>,
}

impl Packet for ClientboundKnownPacks {
    fn encode(&self, buf: &mut BytesMut) -> Result<()> {
        write_varint(buf, self.known_packs_size);
        for pack in &self.known_packs {
            pack.encode(buf)?;
        }
        Ok(())
    }

    fn decode(buf: &mut BytesMut) -> Result<Self> {
        let known_packs_size = read_varint(buf);
        let mut known_packs = Vec::with_capacity(known_packs_size as usize);
        for _ in 0..known_packs_size {
            known_packs.push(Pack::decode(buf)?);
        }
        Ok(Self{known_packs_size , known_packs})
    }

    fn packet_id(&self) -> i32 {
        0x0E
    }
}

pub struct CustomReportDetails {
    pub details_count: i32,
    pub details: Vec<Details>,
}

impl Packet for CustomReportDetails {
    fn encode(&self, buf: &mut BytesMut) -> Result<()> {
        write_varint(buf, self.details_count);
        for detail in &self.details {
            detail.encode(buf)?;
        }
        Ok(())
    }

    fn decode(buf: &mut BytesMut) -> Result<Self> {
        let details_count = read_varint(buf);
        let mut details = Vec::with_capacity(details_count as usize);
        for _ in 0..details_count {
            details.push(Details::decode(buf)?);
        }
        Ok(Self { details_count , details })
    }

    fn packet_id(&self) -> i32 {
        0x0F
    }
}

pub struct ServerLinks {
    pub link_count: i32,
    pub links: Vec<Link>
}

impl Packet for ServerLinks {
    fn encode(&self, buf: &mut BytesMut) -> Result<()> {
        write_varint(buf, self.link_count);
        for link in &self.links {
            link.encode(buf)?;
        }
        Ok(())
    }

    fn decode(buf: &mut BytesMut) -> Result<Self> {
        let link_count = read_varint(buf);
        let mut links = Vec::with_capacity(link_count as usize);
        for _ in 0..link_count {
            links.push(Link::decode(buf)?);
        }
        Ok(Self { link_count, links })
    }

    fn packet_id(&self) -> i32 {
        0x10
    }
}

pub struct ClearDialog {}

impl Packet for ClearDialog {
    fn encode(&self, _buf: &mut BytesMut) -> Result<()> {
        Ok(())
    }

    fn decode(_buf: &mut BytesMut) -> Result<Self> {
       Ok(Self{})
    }

    fn packet_id(&self) -> i32 {
        0x11
    }
}

pub struct ShowDialog {
    pub dialog: Bytes,
}

impl Packet for ShowDialog {
    fn encode(&self, buf: &mut BytesMut) -> Result<()> {
        buf.put_slice(&self.dialog);
        Ok(())
    }

    fn decode(buf: &mut BytesMut) -> Result<Self> {
        let dialog = buf.split().freeze();
        Ok(Self { dialog })
    }

    fn packet_id(&self) -> i32 {
        0x12
    }
}

pub struct CodeOfConduct {
    pub code_of_conduct: Bytes,
}

impl Packet for CodeOfConduct {
    fn encode(&self, buf: &mut BytesMut) -> Result<()> {
        write_string(buf, &self.code_of_conduct);
        Ok(())
    }

    fn decode(buf: &mut BytesMut) -> Result<Self> {
        let code_of_conduct = read_string(buf)?;
        Ok(Self { code_of_conduct })
    }

    fn packet_id(&self) -> i32 {
        0x13
    }
}