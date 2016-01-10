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

package provider.pkgnx.internal;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import provider.pkgnx.NXException;
import provider.pkgnx.util.Decompressor;
import provider.pkgnx.util.SeekableLittleEndianAccessor;

import java.awt.image.BufferedImage;

/**
 * A set of data tables bound to an {@code NXFile}.
 *
 * @author Aaron Weiss
 * @version 1.0.0
 * @since 1/21/14
 */
public abstract class NXTables {
	/**
	 * Looks up a sequence of audio data from the audio table.
	 *
	 * @param index  the starting index of the audio data
	 * @param length the length of the audio data
	 * @return the audio data as a {@code ByteBuf}
	 */
	public abstract ByteBuf getAudioBuf(long index, long length);

	/**
	 * Looks up a bitmap image from the bitmap table.
	 *
	 * @param index  the index of the bitmap
	 * @param width  the width of the image
	 * @param height the height of the image
	 * @return the bitmap as a {@code BufferedImage}
	 */
	public abstract BufferedImage getImage(long index, int width, int height);

	/**
	 * Looks up a string from the string table.
	 *
	 * @param index the index of the string
	 * @return the string
	 */
	public abstract String getString(long index);

	/**
	 * Checks if the offset index is legal.
	 *
	 * @param index the index to check
	 * @throws provider.pkgnx.NXException if the offset index is not legal
	 */
	protected void checkIndex(long index) {
		if (index > Integer.MAX_VALUE)
			throw new NXException("pkgnx cannot support offset indices over " + Integer.MAX_VALUE);
	}

	/**
	 * A lazy-loaded equivalent of {@code ByteBuf}.
	 *
	 * @author Aaron Weiss
	 * @version 1.0
	 * @since 5/27/13
	 */
	protected static class AudioBuf {
		private final SeekableLittleEndianAccessor slea;
		private final long audioOffset;
		private ByteBuf audioBuf;

		/**
		 * Creates a lazy-loaded {@code ByteBuf} for audio.
		 *
		 * @param slea
		 */
		public AudioBuf(SeekableLittleEndianAccessor slea) {
			this.slea = slea;
			audioOffset = slea.getLong();
		}

		/**
		 * Loads a {@code ByteBuf} of the desired {@code length}.
		 *
		 * @param length the length of the audio data
		 * @return the audio buffer
		 */
		public ByteBuf getAudioBuf(long length) {
			if (audioBuf == null) {
				slea.seek(audioOffset);
				audioBuf = Unpooled.wrappedBuffer(slea.getBytes((int) length));
			}
			return audioBuf;
		}
	}

	/**
	 * A lazy-loaded equivalent of {@code BufferedImage}.
	 *
	 * @author Aaron Weiss
	 * @version 1.0
	 * @since 5/27/13
	 */
	protected static class Bitmap {
		private final SeekableLittleEndianAccessor slea;
		private final long bitmapOffset;

		/**
		 * Creates a lazy-loaded {@code BufferedImage}.
		 *
		 * @param slea
		 */
		public Bitmap(SeekableLittleEndianAccessor slea) {
			this.slea = slea;
			bitmapOffset = slea.getLong();
		}

		/**
		 * Loads a {@code BufferedImage} of the desired {@code width} and {@code height}.
		 *
		 * @param width  the width of the image
		 * @param height the height of the image
		 * @return the loaded image
		 */
		public BufferedImage getImage(int width, int height) {
			slea.seek(bitmapOffset);
			ByteBuf image = Unpooled.wrappedBuffer(Decompressor.decompress(slea.getBytes((int) slea.getUnsignedInt()), width * height * 4));
			BufferedImage ret = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			for (int h = 0; h < height; h++) {
				for (int w = 0; w < width; w++) {
					int b = image.readUnsignedByte();
					int g = image.readUnsignedByte();
					int r = image.readUnsignedByte();
					int a = image.readUnsignedByte();
					ret.setRGB(w, h, (a << 24) | (r << 16) | (g << 8) | b);
				}
			}
			return ret;
		}
	}
}
