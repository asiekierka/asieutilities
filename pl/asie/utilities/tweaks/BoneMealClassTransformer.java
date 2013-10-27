package pl.asie.utilities.tweaks;

import java.util.Iterator;

import net.minecraft.item.ItemDye;
import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import pl.asie.utilities.lib.ClassTransformer;

public class BoneMealClassTransformer extends ClassTransformer implements IClassTransformer {
	public static void finish(double treeChance, double mushroomChance, boolean growOnce) {
		try {
			ItemDye.class.getField("treeChance").set(null, treeChance);
			ItemDye.class.getField("mushroomChance").set(null, mushroomChance);
			ItemDye.class.getField("growthChanger").set(null, growOnce ? 8 : 0);
		} catch(Exception e) { e.printStackTrace(); }
	}
	
	public byte[] transform(String className, String newName, byte[] bytecode) {
		if (className.equals("aqf") || className.equals("net.minecraft.block.BlockSapling")) {
			System.out.println("[BonemealClassTransformer] Patching " + className);
			ClassNode sapClass = getClassNode(bytecode);
			MethodNode method = getMethod(sapClass, getName(className, "markOrGrowMarked", "c"),
					getName(className, "(Lnet/minecraft/world/World;IIILjava/util/Random;)V", "(Labv;IIILjava/util/Random;)V"));
			if(method != null) {
				Iterator<AbstractInsnNode> i = method.instructions.iterator();
				while(i.hasNext()) {
					AbstractInsnNode insn = i.next();
					if(insn.getOpcode() == Opcodes.ISTORE && ((VarInsnNode)insn).var == 6) {
						System.out.println("[BonemealClassTrasnformer] Placing growth hook!");
						method.instructions.insertBefore(insn,
								new FieldInsnNode(Opcodes.GETSTATIC, getName(className, "net/minecraft/item/ItemDye", "xk"), "growthChanger", "I"));
						method.instructions.insertBefore(insn, new InsnNode(Opcodes.IOR));
					}
				}
			}
			return writeBytecode(sapClass);
		}
		else if (className.equals("xk") || className.equals("net.minecraft.item.ItemDye")) {
			System.out.println("[BonemealClassTransformer] Patching " + className);
			ClassNode dyeClass = getClassNode(bytecode);
			dyeClass.fields.add(new FieldNode(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "treeChance", "D", null, null));
			dyeClass.fields.add(new FieldNode(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "mushroomChance", "D", null, null));
			dyeClass.fields.add(new FieldNode(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "growthChanger", "I", null, null));
			MethodNode method = getMethod(dyeClass, "applyBonemeal",
					getName(className, "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;IIILnet/minecraft/entity/player/EntityPlayer;)Z",
							"(Lyd;Labv;IIILue;)Z"));
			if(method == null) {
				System.out.println("Mapping bug detected! Listing methods: ");
				for(MethodNode methodd: dyeClass.methods)
					System.out.println("- " + methodd.name + methodd.desc);
				return writeBytecode(dyeClass);
			}
			int step = 0;
			Iterator<AbstractInsnNode> i = method.instructions.iterator();
			while(i.hasNext()) {
				AbstractInsnNode insn = i.next();
				if(insn.getOpcode() == Opcodes.DCMPG) {
					step++;
					String name = "treeChance";
					if(step == 2) name = "mushroomChance";
					method.instructions.remove(method.instructions.get(method.instructions.indexOf(insn)-1));
					method.instructions.insertBefore(insn, new FieldInsnNode(Opcodes.GETSTATIC, className.replace('.', '/'), name, "D"));
				}
			}
			return writeBytecode(dyeClass);
		}
		return bytecode;
	}
}
