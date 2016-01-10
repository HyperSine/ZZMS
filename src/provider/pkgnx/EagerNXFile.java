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

import provider.pkgnx.internal.EagerNXTables;
import provider.pkgnx.internal.NXHeader;
import provider.pkgnx.util.NodeParser;
import provider.pkgnx.util.SeekableLittleEndianAccessor;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * An eager-loaded memory-mapped file for reading specification-compliant NX files.
 *
 * @author Aaron Weiss
 * @version 3.0.0
 * @since 5/26/13
 */
public class EagerNXFile extends NXFile {
	private final SeekableLittleEndianAccessor slea;
	private boolean parsed;
	private NXNode[] nodes;

	/**
	 * Creates a new {@code EagerNXFile} from the specified {@code path}.
	 *
	 * @param path the absolute or relative path to the file
	 * @throws IOException if something goes wrong in reading the file
	 */
	public EagerNXFile(String path) throws IOException {
		this(Paths.get(path));
	}

	/**
	 * Creates a new {@code EagerNXFile} from the specified {@code path}.
	 *
	 * @param path the absolute or relative path to the file
	 * @throws IOException if something goes wrong in reading the file
	 */
	public EagerNXFile(Path path) throws IOException {
		this(path, true);
	}

	/**
	 * Creates a new {@code EagerNXFile} from the specified {@code path} with the option to parse later.
	 *
	 * @param path              the absolute or relative path to the file
	 * @param parsedImmediately whether or not to parse all nodes immediately
	 * @throws IOException if something goes wrong in reading the file
	 */
	public EagerNXFile(String path, boolean parsedImmediately) throws IOException {
		this(Paths.get(path), parsedImmediately);
	}

	/**
	 * Creates a new {@code EagerNXFile} from the specified {@code path} with the option to parse later.
	 *
	 * @param path              the absolute or relative path to the file
	 * @param parsedImmediately whether or not to parse the file immediately
	 * @throws IOException if something goes wrong in reading the file
	 */
	public EagerNXFile(Path path, boolean parsedImmediately) throws IOException {
		super(path.toString());
		FileChannel channel = FileChannel.open(path);
		slea = new SeekableLittleEndianAccessor(channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size()));
		if (parsedImmediately)
			parse();
	}

	/**
	 * Parses the file completely.
	 */
	public void parse() {
		if (parsed)
			return;
		header = new NXHeader(this, slea);
		nodes = new NXNode[(int) header.getNodeCount()];
		tables = new EagerNXTables(header, slea);
		populateNodesTable();
		parsed = true;
		populateNodeChildren();
	}

	/**
	 * Populates the node table by parsing all nodes.
	 */
	private void populateNodesTable() {
		slea.seek(header.getNodeOffset());
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = NodeParser.parseNode(this, slea);
		}
	}

	/**
	 * Populates the children of all nodes.
	 */
	private void populateNodeChildren() {
		for (NXNode node : nodes) {
			node.populateChildren();
		}
	}

	@Override
	public NXNode getNode(int index) {
		parse();
		return nodes[index];
	}
}
