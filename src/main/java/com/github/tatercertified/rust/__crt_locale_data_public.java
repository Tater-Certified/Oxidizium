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
 * struct __crt_locale_data_public {
 *     const unsigned short *_locale_pctype;
 *     int _locale_mb_cur_max;
 *     unsigned int _locale_lc_codepage;
 * }
 * }
 */
public class __crt_locale_data_public {

    __crt_locale_data_public() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        lib_h.C_POINTER.withName("_locale_pctype"),
        lib_h.C_INT.withName("_locale_mb_cur_max"),
        lib_h.C_INT.withName("_locale_lc_codepage")
    ).withName("__crt_locale_data_public");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final AddressLayout _locale_pctype$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("_locale_pctype"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * const unsigned short *_locale_pctype
     * }
     */
    public static final AddressLayout _locale_pctype$layout() {
        return _locale_pctype$LAYOUT;
    }

    private static final long _locale_pctype$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * const unsigned short *_locale_pctype
     * }
     */
    public static final long _locale_pctype$offset() {
        return _locale_pctype$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * const unsigned short *_locale_pctype
     * }
     */
    public static MemorySegment _locale_pctype(MemorySegment struct) {
        return struct.get(_locale_pctype$LAYOUT, _locale_pctype$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * const unsigned short *_locale_pctype
     * }
     */
    public static void _locale_pctype(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(_locale_pctype$LAYOUT, _locale_pctype$OFFSET, fieldValue);
    }

    private static final OfInt _locale_mb_cur_max$LAYOUT = (OfInt)$LAYOUT.select(groupElement("_locale_mb_cur_max"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int _locale_mb_cur_max
     * }
     */
    public static final OfInt _locale_mb_cur_max$layout() {
        return _locale_mb_cur_max$LAYOUT;
    }

    private static final long _locale_mb_cur_max$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int _locale_mb_cur_max
     * }
     */
    public static final long _locale_mb_cur_max$offset() {
        return _locale_mb_cur_max$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int _locale_mb_cur_max
     * }
     */
    public static int _locale_mb_cur_max(MemorySegment struct) {
        return struct.get(_locale_mb_cur_max$LAYOUT, _locale_mb_cur_max$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int _locale_mb_cur_max
     * }
     */
    public static void _locale_mb_cur_max(MemorySegment struct, int fieldValue) {
        struct.set(_locale_mb_cur_max$LAYOUT, _locale_mb_cur_max$OFFSET, fieldValue);
    }

    private static final OfInt _locale_lc_codepage$LAYOUT = (OfInt)$LAYOUT.select(groupElement("_locale_lc_codepage"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * unsigned int _locale_lc_codepage
     * }
     */
    public static final OfInt _locale_lc_codepage$layout() {
        return _locale_lc_codepage$LAYOUT;
    }

    private static final long _locale_lc_codepage$OFFSET = 12;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * unsigned int _locale_lc_codepage
     * }
     */
    public static final long _locale_lc_codepage$offset() {
        return _locale_lc_codepage$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * unsigned int _locale_lc_codepage
     * }
     */
    public static int _locale_lc_codepage(MemorySegment struct) {
        return struct.get(_locale_lc_codepage$LAYOUT, _locale_lc_codepage$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * unsigned int _locale_lc_codepage
     * }
     */
    public static void _locale_lc_codepage(MemorySegment struct, int fieldValue) {
        struct.set(_locale_lc_codepage$LAYOUT, _locale_lc_codepage$OFFSET, fieldValue);
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

