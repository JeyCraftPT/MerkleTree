package ubi;

import java.util.ArrayList;
import java.util.List;
import utils.DigestUtils;
import utils.Utils;

public class MerkleTree {
    private final List<String> transactions;
    private final DigestUtils digestUtils;
    private final int levels;
    public final MerkleNode root;

    public MerkleTree(List<String> transactions, DigestUtils digestUtils) {
        System.out.println("Initializing Merkle Tree...");

        this.digestUtils = digestUtils;
        this.transactions = Utils.extendTillPowerOf2(transactions, "");

        this.levels = (int) (Math.log(this.transactions.size()) / Math.log(2));
        System.out.println("Levels: " + levels + ", Transactions: " + this.transactions.size());

        this.root = buildMerkleTree(this.transactions);
    }

    private MerkleNode buildMerkleTree(List<String> txList) {
        List<MerkleNode> nodes = MerkleNode.getLeafNodes(txList, digestUtils);

        while (nodes.size() > 1) {
            List<MerkleNode> newLevel = new ArrayList<>();
            for (int i = 0; i < nodes.size(); i += 2) {
                newLevel.add(new MerkleNode(nodes.get(i), nodes.get(i + 1), digestUtils));
            }
            nodes = newLevel;
        }
        return nodes.get(0);
    }

    public void printTree() {
        if (root == null) return;

        int depth = levels + 1;
        List<MerkleNode> current = new ArrayList<>();
        current.add(root);

        int maxWidth = (int) Math.pow(2, depth) * 4;

        for (int level = 0; level < depth; level++) {
            int spaceBetween = maxWidth / current.size();

            for (MerkleNode node : current) {
                int padding = spaceBetween / 2 - 2;
                printWhitespaces(padding);
                System.out.print(node != null ? node.hash.substring(0, 4) : "    ");
                printWhitespaces(padding);
            }
            System.out.println();

            List<MerkleNode> next = new ArrayList<>();
            for (MerkleNode node : current) {
                if (node != null) {
                    next.add(node.left);
                    next.add(node.right);
                } else {
                    next.add(null);
                    next.add(null);
                }
            }
            current = next;
        }
    }

    private void printWhitespaces(int count) {
        System.out.print(" ".repeat(Math.max(0, count)));
    }

    public List<String> getMerkleProof(String tx) {
        List<MerkleNode> nodes = MerkleNode.getLeafNodes(transactions, digestUtils);
        List<String> proof = new ArrayList<>();

        int index = transactions.indexOf(tx);
        if (index == -1) return proof;

        while (nodes.size() > 1) {
            List<MerkleNode> newLevel = new ArrayList<>();
            for (int i = 0; i < nodes.size(); i += 2) {
                MerkleNode left = nodes.get(i);
                MerkleNode right = nodes.get(i + 1);
                MerkleNode parent = new MerkleNode(left, right, digestUtils);

                if (i == index || i + 1 == index) {
                    proof.add(i == index ? right.hash : left.hash);
                    index = newLevel.size();
                }
                newLevel.add(parent);
            }
            nodes = newLevel;
        }
        return proof;
    }

    public boolean verifyProof(String tx, List<String> proof, String rootHash) {
        String computed = digestUtils.getHash(tx);

        for (String sibling : proof) {
            if (Utils.compare(computed, sibling)) {
                computed = digestUtils.getHash(computed + sibling);
            } else {
                computed = digestUtils.getHash(sibling + computed);
            }
        }
        return computed.equals(rootHash);
    }
}
