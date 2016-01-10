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
package provider.pkgnx.nodes;

import io.netty.buffer.ByteBuf;
import provider.pkgnx.NXFile;
import provider.pkgnx.NXNode;
import provider.pkgnx.util.SeekableLittleEndianAccessor;

/**
 * An {@code NXNode} representing an Audio {@code ByteBuf}.
 *
 * @author Aaron Weiss
 * @version 3.0.0
 * @since 5/27/13
 */
public class NXAudioNode extends NXNode {
	private final long mp3Index, length;

	/**
	 * Creates a new {@code NXAudioNode}.
	 *
	 * @param name       the name of the node
	 * @param file       the file the node is from
	 * @param childIndex the index of the first child of the node
	 * @param childCount the number of children
	 * @param slea       the {@code SeekableLittleEndianAccessor} to read from
	 */
	public NXAudioNode(String name, NXFile file, long childIndex, int childCount, SeekableLittleEndianAccessor slea) {
		super(name, file, childIndex, childCount);
		mp3Index = slea.getUnsignedInt();
		length = slea.getUnsignedInt();
	}

	@Override
	public ByteBuf get() {
		return getAudioBuf();
	}

	/**
	 * Gets the value of this node as a {@code ByteBuf}.
	 *
	 * @return the node value
	 */
	public ByteBuf getAudioBuf() {
		if (file.getHeader().getSoundCount() == 0)
			return null;
		return file.getTables().getAudioBuf(mp3Index, length);
	}


	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		else if (!(obj instanceof NXAudioNode))
			return false;
		else
			return obj == this || (((NXNode) obj).getName().equals(getName()) &&
					((NXNode) obj).getChildCount() == getChildCount() &&
					((NXNode) obj).getFirstChildIndex() == getFirstChildIndex() &&
					((NXAudioNode) obj).mp3Index == mp3Index &&
					((NXAudioNode) obj).length == length);
	}
}
