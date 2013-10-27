package pl.asie.utilities.interop;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import pl.asie.utilities.lib.ClassTransformer;

public class InteropClassTransformer extends ClassTransformer implements IClassTransformer {
	public byte[] transform(String className, String newName, byte[] bytecode) {
		if (className.equals("dan200.computer.shared.TileEntityComputer")) {
			System.out.println("[InteropClassTransformer] Patching " + className);
			ClassNode computerClass = getClassNode(bytecode);
			//transformComputerCC(computerClass, className); TODO!
			return writeBytecodeFrames(computerClass);
		}
		return bytecode;
	}
	
	public void transformComputerCC(ClassNode cn, String className) {
		cn.interfaces.add("mods/immibis/redlogic/api/wiring/IBundledUpdatable");
		// Add RedLogic handler.
		String klazz = className.replace('.', '/');
		MethodVisitor mv = cn.visitMethod(Opcodes.ACC_PUBLIC, "onBundledInputChanged", "()V", null, null);
		mv.visitCode();
		Label l1 = new Label();
		mv.visitLabel(l1);
		mv.visitInsn(Opcodes.ICONST_0);
		mv.visitVarInsn(Opcodes.ISTORE, 1);
		Label l2 = new Label();
		mv.visitJumpInsn(Opcodes.GOTO, l2);
		Label l3 = new Label();
		mv.visitLabel(l3);
		mv.visitFrame(Opcodes.F_APPEND,1, new Object[] {Opcodes.INTEGER}, 0, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntity", "worldObj", "Lnet/minecraft/world/World;");
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntity", "xCoord", "I");
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntity", "yCoord", "I");
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntity", "zCoord", "I");
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "pl/asie/utilities/interop/WireStrength", "getBundledPowerInput", "(Lnet/minecraft/world/World;IIII)I");
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, klazz, "setBundledPowerInput", "(II)V");
		Label l4 = new Label();
		mv.visitLabel(l4);
		mv.visitLineNumber(13, l4);
		mv.visitIincInsn(1, 1);
		mv.visitLabel(l2);
		mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
		mv.visitVarInsn(Opcodes.ILOAD, 1);
		mv.visitIntInsn(Opcodes.BIPUSH, 6);
		mv.visitJumpInsn(Opcodes.IF_ICMPLT, l3);
		mv.visitInsn(Opcodes.RETURN);
		Label l6 = new Label();
		mv.visitLabel(l6);
		mv.visitLocalVariable("this", "L"+klazz+";", null, l1, l6, 0);
		mv.visitLocalVariable("i", "I", null, l1, l6, 1);
		mv.visitMaxs(7, 2);
		mv.visitEnd();
	}
}
