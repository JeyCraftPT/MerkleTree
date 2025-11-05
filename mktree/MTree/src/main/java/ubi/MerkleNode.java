package ubi;

import java.util.ArrayList;
import java.util.List;
import utils.DigestUtils;
import utils.Utils;

public class MerkleNode {
    public final String hash;
    public final MerkleNode left;
    public final MerkleNode right;


    public MerkleNode(String hash) {
        this.hash = hash;
        this.left = null;
        this.right = null;
    }

    public MerkleNode(MerkleNode left, MerkleNode right, DigestUtils digestUtils) {
        this.left = left;
        this.right = right;

        // Ensure deterministic ordering when concatenating hashes
        if (Utils.compare(left.hash, right.hash)) {
            this.hash = digestUtils.getHash(left.hash + right.hash);
        } else {
            this.hash = digestUtils.getHash(right.hash + left.hash);
        }
    }

    public static List<MerkleNode> getLeafNodes(List<String> transactions, DigestUtils digestUtils) {
        List<String> hashes = digestUtils.getHashList(transactions);
        List<MerkleNode> nodes = new ArrayList<>();

        for (String hash : hashes) {
            nodes.add(new MerkleNode(hash));
        }
        return nodes;
    }
}
