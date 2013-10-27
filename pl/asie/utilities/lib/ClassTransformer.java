package pl.asie.utilities.lib;

import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;

import net.minecraft.launchwrapper.IClassTransformer;

public class ClassTransformer implements IClassTransformer {

	@Override
	public byte[] transform(String arg0, String arg1, byte[] arg2) {
		return arg2;
	}
	
	public static boolean isObfuscated(String name) {
		return !name.contains("."); // HACK, but it works.
	}
	
	public static LabelNode newLabelNode() {
		Label l = new Label();
		LabelNode ln = new LabelNode(l);
		l.info = ln;
		return ln;
	}
	
	public static ClassNode getClassNode(byte[] bytes) {
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);
		return classNode;
	}
	
	public static byte[] writeBytecode(ClassNode node) {
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		node.accept(writer);
		return writer.toByteArray();
	}
	
	public static byte[] writeBytecodeFrames(ClassNode node) {
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		node.accept(writer);
		return writer.toByteArray();
	}
	
	public static String getName(String className, String deobfName, String obfName) {
		return (isObfuscated(className) ? obfName : deobfName);
	}
	
	public static MethodNode getMethod(ClassNode node, String mName, String mSig) {
		Iterator<MethodNode> methods = node.methods.iterator();
		while(methods.hasNext()) {
			MethodNode m = methods.next();
			if (m.name.equals(mName) && m.desc.equals(mSig)) {
				return m;
			}
		}
		return null;
	}
	public static FieldNode getField(ClassNode node, String mName, String mSig) {
		Iterator<FieldNode> fields = node.fields.iterator();
		while(fields.hasNext()) {
			FieldNode f = fields.next();
			if (f.name.equals(mName) && f.desc.equals(mSig)) {
				return f;
			}
		}
		return null;
	}
}
