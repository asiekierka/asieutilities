package pl.asie.utilities.skin;

import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import cpw.mods.fml.common.asm.transformers.DeobfuscationTransformer;
import pl.asie.utilities.AsieUtilities;
import pl.asie.utilities.lib.ClassTransformer;
import net.minecraft.launchwrapper.IClassTransformer;

public class SkinClassTransformer extends ClassTransformer implements IClassTransformer {
	public byte[] transform(String className, String newName, byte[] bytecode) {
		//System.out.println("[SkinClassTransformer] Checking " + className);
		if (className.equals("ber") || className.equals("net.minecraft.client.entity.AbstractClientPlayer")) {
			System.out.println("[SkinClassTransformer] Patching " + className);
			ClassNode skinClass = getClassNode(bytecode);
			skinClass.fields.add(new FieldNode(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "skinURL", "Ljava/lang/String;", null, null));
			skinClass.fields.add(new FieldNode(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "capeURL", "Ljava/lang/String;", null, null));
			// Patch methods
			MethodNode method = getMethod(skinClass, getName(className, "getSkinUrl", "d"), "(Ljava/lang/String;)Ljava/lang/String;");
			if(method != null) patchURL(className, method, "skinURL");
			method = getMethod(skinClass, getName(className, "getCapeUrl", "e"), "(Ljava/lang/String;)Ljava/lang/String;");
			if(method != null) patchURL(className, method, "capeURL");
			method = getMethod(skinClass, getName(className, "setupCustomSkin", "l"), "()V");
			if(method != null) patchSetup(method);
			return writeBytecode(skinClass);
		}
		return bytecode;
	}
	
	public void patchSetup(MethodNode method) {
		method.access = method.access & ~(Opcodes.ACC_PROTECTED | Opcodes.ACC_PRIVATE) | Opcodes.ACC_PUBLIC;
		LabelNode ln = newLabelNode();
		AbstractInsnNode start = method.instructions.getFirst();
		// Add preInitialization handler
		method.instructions.insertBefore(start, new VarInsnNode(Opcodes.ALOAD, 0));
		method.instructions.insertBefore(start, new MethodInsnNode(Opcodes.INVOKESTATIC, "pl/asie/utilities/skin/SkinHandler", "onPreInit", "(Lnet/minecraft/client/entity/AbstractClientPlayer;)Z"));
		method.instructions.insertBefore(start, new JumpInsnNode(Opcodes.IFNE, ln));
		method.instructions.insertBefore(start, new InsnNode(Opcodes.RETURN));
		method.instructions.insertBefore(start, ln);
		// Add postInitialization handler
		AbstractInsnNode end = method.instructions.get(method.instructions.size() - 2);
		method.instructions.insertBefore(end, new VarInsnNode(Opcodes.ALOAD, 0));
		method.instructions.insertBefore(end, new MethodInsnNode(Opcodes.INVOKESTATIC, "pl/asie/utilities/skin/SkinHandler", "onPostInit", "(Lnet/minecraft/client/entity/AbstractClientPlayer;)Z"));
		method.instructions.insertBefore(end, new InsnNode(Opcodes.POP));
		System.out.println("[SkinClassTransformer] setupCustomSkin patched.");
	}
	
	public void patchURL(String className, MethodNode method, String url) {
		AbstractInsnNode currentNode = null;
		AbstractInsnNode targetNode = null;

		@SuppressWarnings("unchecked")
		Iterator<AbstractInsnNode> iter = method.instructions.iterator();

		int index = -1;
		boolean patched = false;
		while (!patched && iter.hasNext()) {
			index++;
			currentNode = iter.next();
			if(currentNode.getOpcode() == Opcodes.LDC) {
				method.instructions.insert(currentNode, new FieldInsnNode(Opcodes.GETSTATIC, className.replace('.', '/'), url, "Ljava/lang/String;"));
				method.instructions.remove(currentNode);
				System.out.println("[SkinClassTransformer] URL in " + method.name + " ("+url+") patched.");
				patched = true;
				break;
			}
		}
	}
}
