/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2014 Aaron Weiss
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package provider.pkgnx.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import provider.pkgnx.NXException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * An indexed accessor for reading Little Endian-formatted data.
 *
 * @author Aaron Weiss
 * @version 1.1.0
 * @since 5/26/13
 */
public class SeekableLittleEndianAccessor {
	private final static Logger logger = LoggerFactory.getLogger(SeekableLittleEndianAccessor.class);
	private static final ThreadLocal<CharsetDecoder> utfDecoder = new ThreadLocal<CharsetDecoder>() {
		@Override
		protected CharsetDecoder initialValue() {
			return Charset.forName("UTF-8").newDecoder();
		}
	};
	private final ByteBuf buf;
	private final ThreadLocal<ByteBuf> localBuf;

	/**
	 * Creates an immutable {@code SeekableLittleEndianAccessor} from an array of bytes.
	 *
	 * @param bytes the array to use
	 */
	public SeekableLittleEndianAccessor(byte[] bytes) {
		this(Unpooled.wrappedBuffer(bytes));
	}

	/**
	 * Creates an immutable {@code SeekableLittleEndianAccessor} from an NIO {@code ByteBuffer}.
	 *
	 * @param buf the buffer to use
	 */
	public SeekableLittleEndianAccessor(ByteBuffer buf) {
		this(Unpooled.wrappedBuffer(buf));
	}

	/**
	 * Creates an immutable {@code SeekableLittleEndianAccessor} wrapping a {@code ByteBuf}.
	 *
	 * @param buf the buffer to wrap
	 */
	public SeekableLittleEndianAccessor(final ByteBuf buf) {
		this.buf = buf.order(ByteOrder.LITTLE_ENDIAN);
		localBuf = new ThreadLocal<ByteBuf>() {
			@Override
			protected ByteBuf initialValue() {
				return SeekableLittleEndianAccessor.this.buf.duplicate();
			}
		};
	}

	/**
	 * Gets the internal {@code ByteBuf} used by this accessor.
	 *
	 * @return the internal buffer
	 */
	public ByteBuf getBuf() {
		return localBuf.get();
	}

	/**
	 * Skips the desired {@code length} in the buffer.
	 *
	 * @param length the number of bytes to skip
	 * @see io.netty.buffer.ByteBuf#skipBytes(int)
	 */
	public void skip(int length) {
		localBuf.get().skipBytes(length);
	}

	/**
	 * Moves to the desired {@code offset} in the buffer.
	 *
	 * @param offset the offset to move to
	 * @throws NXException if the offset is greater than Integer.MAX_VALUE
	 */
	public void seek(long offset) {
		if (offset > Integer.MAX_VALUE)
			throw new NXException("Cannot seek to desired offset (" + offset + ") due to limitations with ByteBuf.");
		seek((int) offset);
	}

	/**
	 * Moves to the desired {@code offset} in the buffer.
	 *
	 * @param offset the offset to move to
	 * @see io.netty.buffer.ByteBuf#readerIndex(int)
	 */
	public void seek(int offset) {
		localBuf.get().readerIndex(offset);
	}

	/**
	 * Marks the current index to be returned to later.
	 */
	public void mark() {
		localBuf.get().markReaderIndex();
	}

	/**
	 * Seeks back to the last marked index.
	 */
	public void reset() {
		localBuf.get().resetReaderIndex();
	}

	/**
	 * Reads the next byte from the buffer.
	 *
	 * @return the next byte
	 *
	 * @see io.netty.buffer.ByteBuf#readByte()
	 */
	public byte getByte() {
		return localBuf.get().readByte();
	}

	/**
	 * Reads the next unsigned byte from the buffer.
	 *
	 * @return the next unsigned byte
	 *
	 * @see io.netty.buffer.ByteBuf#readUnsignedByte()
	 */
	public short getUnsignedByte() {
		return localBuf.get().readUnsignedByte();
	}

	/**
	 * Reads the next short from the buffer.
	 *
	 * @return the next short
	 *
	 * @see io.netty.buffer.ByteBuf#readShort()
	 */
	public short getShort() {
		return localBuf.get().readShort();
	}

	/**
	 * Reads the next unsigned short from the buffer.
	 *
	 * @return the next unsigned short
	 *
	 * @see io.netty.buffer.ByteBuf#readUnsignedShort()
	 */
	public int getUnsignedShort() {
		return localBuf.get().readUnsignedShort();
	}

	/**
	 * Reads the next integer from the buffer.
	 *
	 * @return the next integer
	 *
	 * @see io.netty.buffer.ByteBuf#readInt()
	 */
	public int getInt() {
		return localBuf.get().readInt();
	}

	/**
	 * Reads the next unsigned integer from the buffer.
	 *
	 * @return the next unsigned integer
	 *
	 * @see io.netty.buffer.ByteBuf#readUnsignedInt()
	 */
	public long getUnsignedInt() {
		return localBuf.get().readUnsignedInt();
	}

	/**
	 * Reads the next long from the buffer.
	 *
	 * @return the next long
	 *
	 * @see io.netty.buffer.ByteBuf#readLong()
	 */
	public long getLong() {
		return localBuf.get().readLong();
	}

	/**
	 * Reads the next float  from the buffer.
	 *
	 * @return the next float
	 *
	 * @see io.netty.buffer.ByteBuf#readFloat()
	 */
	public float getFloat() {
		return localBuf.get().readFloat();
	}

	/**
	 * Reads the next double from the buffer.
	 *
	 * @return the next double
	 *
	 * @see io.netty.buffer.ByteBuf#readDouble()
	 */
	public double getDouble() {
		return localBuf.get().readDouble();
	}

	/**
	 * Reads the next {@code length} bytes from the buffer.
	 *
	 * @param length the length to read
	 * @return an array of bytes read
	 *
	 * @see io.netty.buffer.ByteBuf#readBytes(int)
	 */
	public byte[] getBytes(int length) {
		byte[] ret = new byte[length];
		localBuf.get().readBytes(ret);
		return ret;
	}

	/**
	 * Reads the next {@code length} bytes from the buffer.
	 *
	 * @param length the length to read
	 * @return a buffer of read bytes
	 *
	 * @see io.netty.buffer.ByteBuf#readBytes(io.netty.buffer.ByteBuf)
	 */
	public ByteBuf getBuf(int length) {
		ByteBuf ret = Unpooled.buffer(length);
		localBuf.get().readBytes(ret);
		return ret;
	}

	/**
	 * Gets the next UTF String from the buffer using the next unsigned short as the length.
	 *
	 * @return the next string
	 */
	public String getUTFString() {
		return getUTFString(getUnsignedShort());
	}

	/**
	 * Gets the next UTF String of length {@code length} from the buffer.
	 *
	 * @param length the length of the string
	 * @return the next string
	 */
	public String getUTFString(int length) {
		try {
			byte[] data = getBytes(length);
			return utfDecoder.get().decode(ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN)).toString();
		} catch (CharacterCodingException e) {
			logger.error("Failed to load UTF String in buffer.", e);
		}
		return null;
	}
}
