package org.cytoscape.rest;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

import javax.xml.soap.Node;

import org.cytoscape.model.CyIdentifiable;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.NetworkTestSupport;
import org.cytoscape.rest.internal.translator.JSON2CyNetworkTranslator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JSON2CyNetworkTranslatorTest {

	private String json1 = "";
	
	private NetworkTestSupport support = new NetworkTestSupport();
	
	
	@Before
	public void setUp() throws Exception {
		readSamples();
	}
	

	private final void readSamples() throws IOException {

		FileReader in = new FileReader("./src/test/resources/json/galFiltered.json");
		BufferedReader br = new BufferedReader(in);
		String line;
		StringBuilder builder = new StringBuilder();
		while ((line = br.readLine()) != null)
			builder.append(line);
		
		br.close();
		in.close();

		json1 = builder.toString();
		
	}

	@After
	public void tearDown() throws Exception {
	}

	

	@Test
	public void testTranslate() throws Exception {
		
		System.out.println(json1);
		
		assertNotNull(json1);
		final JSON2CyNetworkTranslator translator = new JSON2CyNetworkTranslator(support.getNetworkFactory());
		
		CyNetwork network = translator.translate(json1);
		
		assertNotNull(network);
		assertEquals(331, network.getNodeCount());
		assertEquals(362, network.getEdgeCount());
		
		Collection<CyRow> rows = network.getDefaultNodeTable().getMatchingRows(CyNetwork.NAME, "YGL134W");
		assertNotNull(rows);
		assertEquals(1, rows.size());
		final CyRow row1 = rows.iterator().next();
		final Long suid = row1.get(CyIdentifiable.SUID, Long.class);
		CyNode node = network.getNode(suid);
		assertNotNull(node);
		assertEquals("YGL134W", network.getRow(node).get(CyNetwork.NAME, String.class));
	}
}
