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
package provider.pkgnx;

import provider.pkgnx.internal.NXHeader;
import provider.pkgnx.internal.NXTables;

/**
 * The basic specification for an NX file implementation.
 *
 * @author Aaron Weiss
 * @version 1.0.0
 * @since 12/12/13
 */
public abstract class NXFile {
	protected final String filePath;
	protected NXHeader header;
	protected NXTables tables;

	/**
	 * Constructs an NXFile from its file path.
	 *
	 * @param filePath
	 */
	protected NXFile(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * Gets the path to this {@code NXFile}.
	 *
	 * @return the path to this file
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * Gets the {@code NXHeader} of this file.
	 *
	 * @return this file's header
	 */
	public NXHeader getHeader() {
		return header;
	}

	/**
	 * Gets the {@code NXTables} from this file.
	 *
	 * @return this file's offset tables
	 */
	public NXTables getTables() {
		return tables;
	}

	/**
	 * Gets the root {@code NXNode} of the file.
	 *
	 * @return the file's root node
	 */
	public NXNode getRoot() {
		return getNode(0);
	}

	/**
	 * Gets an {@code NXNode} by {@code index} from the internal node table of this file.
	 * n.b. the node at index 0 must always be the root node.
	 *
	 * @param index the index of the node
	 * @return the desired node
	 */
	public abstract NXNode getNode(int index);

	/**
	 * Resolves the desired {@code path} to an {@code NXNode}.
	 *
	 * @param path the path to the node
	 * @return the desired node
	 */
	public NXNode resolve(String path) {
		if (path.equals("/"))
			return getRoot();
		return resolve(path.split("/"));
	}

	/**
	 * Resolves the desired {@code path} to an {@code NXNode}.
	 *
	 * @param path the path to the node
	 * @return the desired node
	 */
	public NXNode resolve(String[] path) {
		NXNode cursor = getRoot();
		for (int i = 0; i < path.length; i++) {
			if (cursor == null)
				return null;
			cursor = cursor.getChild(path[i]);
		}
		return cursor;
	}
}
