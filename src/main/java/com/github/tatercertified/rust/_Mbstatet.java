// Generated by jextract

package com.github.tatercertified.rust;

import java.lang.invoke.*;
import java.lang.foreign.*;
import java.nio.ByteOrder;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import static java.lang.foreign.ValueLayout.*;
import static java.lang.foreign.MemoryLayout.PathElement.*;

/**
 * {@snippet lang=c :
 * struct _Mbstatet {
 *     unsigned long _Wchar;
 *     unsigned short _Byte;
 *     unsigned short _State;
 * }
 * }
 */
public class _Mbstatet {

    _Mbstatet() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        lib_h.C_LONG.withName("_Wchar"),
        lib_h.C_SHORT.withName("_Byte"),
        lib_h.C_SHORT.withName("_State")
    ).withName("_Mbstatet");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfInt _Wchar$LAYOUT = (OfInt)$LAYOUT.select(groupElement("_Wchar"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * unsigned long _Wchar
     * }
     */
    public static final OfInt _Wchar$layout() {
        return _Wchar$LAYOUT;
    }

    private static final long _Wchar$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * unsigned long _Wchar
     * }
     */
    public static final long _Wchar$offset() {
        return _Wchar$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * unsigned long _Wchar
     * }
     */
    public static int _Wchar(MemorySegment struct) {
        return struct.get(_Wchar$LAYOUT, _Wchar$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * unsigned long _Wchar
     * }
     */
    public static void _Wchar(MemorySegment struct, int fieldValue) {
        struct.set(_Wchar$LAYOUT, _Wchar$OFFSET, fieldValue);
    }

    private static final OfShort _Byte$LAYOUT = (OfShort)$LAYOUT.select(groupElement("_Byte"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * unsigned short _Byte
     * }
     */
    public static final OfShort _Byte$layout() {
        return _Byte$LAYOUT;
    }

    private static final long _Byte$OFFSET = 4;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * unsigned short _Byte
     * }
     */
    public static final long _Byte$offset() {
        return _Byte$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * unsigned short _Byte
     * }
     */
    public static short _Byte(MemorySegment struct) {
        return struct.get(_Byte$LAYOUT, _Byte$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * unsigned short _Byte
     * }
     */
    public static void _Byte(MemorySegment struct, short fieldValue) {
        struct.set(_Byte$LAYOUT, _Byte$OFFSET, fieldValue);
    }

    private static final OfShort _State$LAYOUT = (OfShort)$LAYOUT.select(groupElement("_State"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * unsigned short _State
     * }
     */
    public static final OfShort _State$layout() {
        return _State$LAYOUT;
    }

    private static final long _State$OFFSET = 6;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * unsigned short _State
     * }
     */
    public static final long _State$offset() {
        return _State$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * unsigned short _State
     * }
     */
    public static short _State(MemorySegment struct) {
        return struct.get(_State$LAYOUT, _State$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * unsigned short _State
     * }
     */
    public static void _State(MemorySegment struct, short fieldValue) {
        struct.set(_State$LAYOUT, _State$OFFSET, fieldValue);
    }

    /**
     * Obtains a slice of {@code arrayParam} which selects the array element at {@code index}.
     * The returned segment has address {@code arrayParam.address() + index * layout().byteSize()}
     */
    public static MemorySegment asSlice(MemorySegment array, long index) {
        return array.asSlice(layout().byteSize() * index);
    }

    /**
     * The size (in bytes) of this struct
     */
    public static long sizeof() { return layout().byteSize(); }

    /**
     * Allocate a segment of size {@code layout().byteSize()} using {@code allocator}
     */
    public static MemorySegment allocate(SegmentAllocator allocator) {
        return allocator.allocate(layout());
    }

    /**
     * Allocate an array of size {@code elementCount} using {@code allocator}.
     * The returned segment has size {@code elementCount * layout().byteSize()}.
     */
    public static MemorySegment allocateArray(long elementCount, SegmentAllocator allocator) {
        return allocator.allocate(MemoryLayout.sequenceLayout(elementCount, layout()));
    }

    /**
     * Reinterprets {@code addr} using target {@code arena} and {@code cleanupAction} (if any).
     * The returned segment has size {@code layout().byteSize()}
     */
    public static MemorySegment reinterpret(MemorySegment addr, Arena arena, Consumer<MemorySegment> cleanup) {
        return reinterpret(addr, 1, arena, cleanup);
    }

    /**
     * Reinterprets {@code addr} using target {@code arena} and {@code cleanupAction} (if any).
     * The returned segment has size {@code elementCount * layout().byteSize()}
     */
    public static MemorySegment reinterpret(MemorySegment addr, long elementCount, Arena arena, Consumer<MemorySegment> cleanup) {
        return addr.reinterpret(layout().byteSize() * elementCount, arena, cleanup);
    }
}

