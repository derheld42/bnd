package test;

import java.io.*;
import java.util.*;

import junit.framework.*;

import org.osgi.framework.namespace.*;
import org.osgi.resource.*;

import aQute.bnd.build.*;
import aQute.bnd.build.model.*;
import aQute.bnd.osgi.resource.*;

public class BndEditModelTest extends TestCase {

	public static void testVariableInRunRequirements() throws Exception {
		Workspace ws = new Workspace(new File("testresources/ws"));
		BndEditModel model = new BndEditModel(ws);
		File f = new File("testresources/ws/p7/reuse.bndrun");
		model.setBndResource(f);
		model.setBndResourceName("reuse.bndrun");
		model.loadFrom(f);

		// VERIFY
		List<Requirement> r = model.getRunRequiresProcessed();
		assertEquals(4, r.size());
		assertEquals("(osgi.identity=variable)", r.get(0).toString());
		assertEquals("(osgi.identity=variable2)", r.get(1).toString());
		assertEquals("(osgi.identity=b)", r.get(2).toString());
		assertEquals("(osgi.identity=c)", r.get(3).toString());

		r = model.getRunRequires();
		assertEquals(3, r.size());
		assertEquals("${var}", r.get(0).toString());
		assertEquals("(osgi.identity=b)", r.get(1).toString());
		assertEquals("(osgi.identity=c)", r.get(2).toString());

		// Test Set with variables
		List<Requirement> rr = new LinkedList<Requirement>();
		CapReqBuilder cp = new CapReqBuilder(IdentityNamespace.IDENTITY_NAMESPACE);
		rr.add(cp.addDirective(Namespace.REQUIREMENT_FILTER_DIRECTIVE, "(osgi.identity=b)").buildSyntheticRequirement());
		rr.add(new RequirementVariable("${var}"));
		model.setRunRequires(rr);

		// VERIFY
		r = model.getRunRequiresProcessed();
		assertEquals(3, r.size());
		assertEquals("(osgi.identity=b)", r.get(0).toString());
		assertEquals("(osgi.identity=variable)", r.get(1).toString());
		assertEquals("(osgi.identity=variable2)", r.get(2).toString());
		r = model.getRunRequires();
		assertEquals(2, r.size());
		assertEquals("(osgi.identity=b)", r.get(0).toString());
		assertEquals("${var}", r.get(1).toString());

		// Test SET
		rr = new LinkedList<Requirement>();
		cp = new CapReqBuilder(IdentityNamespace.IDENTITY_NAMESPACE);
		rr.add(cp.addDirective(Namespace.REQUIREMENT_FILTER_DIRECTIVE, "(osgi.identity=b)").buildSyntheticRequirement());
		rr.add(cp.addDirective(Namespace.REQUIREMENT_FILTER_DIRECTIVE, "(osgi.identity=c)").buildSyntheticRequirement());
		model.setRunRequires(rr);

		// VERIFY
		r = model.getRunRequires();
		assertEquals(2, r.size());
		assertEquals("(osgi.identity=b)", r.get(0).toString());
		assertEquals("(osgi.identity=c)", r.get(1).toString());
		r = model.getRunRequiresProcessed();
		assertEquals(2, r.size());
		assertEquals("(osgi.identity=b)", r.get(0).toString());
		assertEquals("(osgi.identity=c)", r.get(1).toString());
	}
}
