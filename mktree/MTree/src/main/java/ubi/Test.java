package ubi;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import utils.DigestUtils;

public class Test {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        // Choose hashing algorithm
        System.out.println("Choose a hashing algorithm:");
        System.out.println("1. SHA-256");
        System.out.println("2. MD5");
        System.out.println("3. MurmurHash3");
        System.out.println("4. SHA-512");
        System.out.print("Enter algorithm choice (1-4): ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        String algorithm = switch (choice) {
            case 1 -> "sha256";
            case 2 -> "md5";
            case 3 -> "murmur";
            case 4 -> "sha512";
            default -> {
                System.out.println("Invalid choice. Defaulting to SHA-256.");
                yield "sha256";
            }
        };

        DigestUtils digestUtils = new DigestUtils(algorithm);

        System.out.print("\nEnter n (to create 2^n transactions): ");
        int n = scanner.nextInt();

        List<String> transactions = new ArrayList<>();
        for (int i = 0; i < Math.pow(2, n); i++) {
            transactions.add("tx" + (i + 1));
        }

        MerkleTree merkleTree = new MerkleTree(transactions, digestUtils);
        System.out.println("\nMerkle Tree Structure:");
        merkleTree.printTree();

        String targetTx = transactions.get(0);
        List<String> proof = merkleTree.getMerkleProof(targetTx);
        boolean isValid = merkleTree.verifyProof(targetTx, proof, merkleTree.root.hash);

        PrintStream out = System.out;
        String rootPrefix = merkleTree.root.hash.substring(0, 8);

        out.print("\nProof for " + targetTx + ": [ ");
        for (String p : proof) out.print(p.substring(0, 8) + " ");
        out.println("] → Root: " + rootPrefix + " → " + (isValid ? "Validated" : "Invalid"));

        if (!proof.isEmpty()) {
            List<String> tampered = new ArrayList<>(proof);
            tampered.set(0, "00000000");
            boolean tamperedValid = merkleTree.verifyProof(targetTx, tampered, merkleTree.root.hash);
            out.print("Tampered Proof for " + targetTx + ": [ ");
            for (String p : tampered) out.print(p.substring(0, 8) + " ");
            out.println("] → Root: " + rootPrefix + " → " + (tamperedValid ? "Validated" : "Invalid"));
        }
    }
}
