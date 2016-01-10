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
 * A lazy-loaded set of data tables bound to an {@code NXFile}.
 *
 * @author Aaron Weiss
 * @version 1.0.0
 * @since 1/21/14
 */
public class LazyNXTables extends NXTables {
	private final AudioBuf[] audioBufs;
	private final Bitmap[] bitmaps;
	private final String[] strings;
	private final NXHeader header;
	private final SeekableLittleEndianAccessor slea;

	public LazyNXTables(NXHeader header, SeekableLittleEndianAccessor slea) {
		this.header = header;
		this.slea = slea;
		audioBufs = new AudioBuf[(int) header.getSoundCount()];
		bitmaps = new Bitmap[(int) header.getBitmapCount()];
		strings = new String[(int) header.getStringCount()];
	}

	@Override
	public ByteBuf getAudioBuf(long index, long length) {
		checkIndex(index);
		AudioBuf ret = audioBufs[(int) index];
		if (ret != null)
			return ret.getAudioBuf(length);
		try {
			slea.mark();
			slea.seek(header.getSoundOffset() + index * 8);
			return (audioBufs[(int) index] = new AudioBuf(slea)).getAudioBuf(length);
		} finally {
			slea.reset();
		}
	}

	@Override
	public BufferedImage getImage(long index, int width, int height) {
		checkIndex(index);
		Bitmap ret = bitmaps[(int) index];
		if (ret != null)
			return ret.getImage(width, height);
		try {
			slea.mark();
			slea.seek(header.getBitmapOffset() + index * 8);
			return (bitmaps[(int) index] = new Bitmap(slea)).getImage(width, height);
		} finally {
			slea.reset();
		}
	}

	@Override
	public String getString(long index) {
		checkIndex(index);
		String ret = strings[(int) index];
		if (ret != null)
			return ret;
		try {
			slea.mark();
			slea.seek(header.getStringOffset() + index * 8);
			slea.seek(slea.getLong());
			return (strings[(int) index] = slea.getUTFString());
		} finally {
			slea.reset();
		}
	}
}
