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
import provider.pkgnx.util.SeekableLittleEndianAccessor;

import java.awt.image.BufferedImage;

/**
 * An eager-loaded set of data tables bound to an {@code NXFile}.
 *
 * @author Aaron Weiss
 * @version 2.0.0
 * @since 6/26/13
 */
public class EagerNXTables extends NXTables {
	private final AudioBuf[] audioBufs;
	private final Bitmap[] bitmaps;
	private final String[] strings;

	/**
	 * Creates a set of {@code EagerNXTables}.
	 *
	 * @param header the header of the {@code NXFile}.
	 * @param slea   the accessor to read from
	 */
	public EagerNXTables(NXHeader header, SeekableLittleEndianAccessor slea) {
		slea.seek(header.getSoundOffset());
		audioBufs = new AudioBuf[(int) header.getSoundCount()];
		for (int i = 0; i < audioBufs.length; i++)
			audioBufs[i] = new AudioBuf(slea);

		slea.seek(header.getBitmapOffset());
		bitmaps = new Bitmap[(int) header.getBitmapCount()];
		for (int i = 0; i < bitmaps.length; i++)
			bitmaps[i] = new Bitmap(slea);

		slea.seek(header.getStringOffset());
		strings = new String[(int) header.getStringCount()];
		for (int i = 0; i < strings.length; i++) {
			long offset = slea.getLong();
			slea.mark();
			slea.seek(offset);
			strings[i] = slea.getUTFString();
			slea.reset();
		}
	}

	@Override
	public ByteBuf getAudioBuf(long index, long length) {
		checkIndex(index);
		return audioBufs[(int) index].getAudioBuf(length);
	}

	@Override
	public BufferedImage getImage(long index, int width, int height) {
		checkIndex(index);
		return bitmaps[(int) index].getImage(width, height);
	}

	@Override
	public String getString(long index) {
		checkIndex(index);
		return strings[(int) index];
	}
}
