/**
 * Provides functionality to merge multiple n-ary trees of {@link ProductionElement}.
 * The merge operation integrates subtrees into their parent trees recursively.
 */
package project.domain.service;

import project.domain.genericDataStructures.NaryTree;
import project.domain.genericDataStructures.NaryTreeNode;
import project.domain.model.ProductionElement;

import java.util.List;

public class MergeTrees {

    /**
     * Merges a list of n-ary trees of {@link ProductionElement}.
     * For each tree in the list, it iterates through the children of the root node.
     * If a child node contains a subtree (represented as another {@link NaryTree}),
     * that subtree is merged into the parent tree at the corresponding child node.
     *
     * @param trees The list of n-ary trees to be merged.
     */
    public static void merge(List<NaryTree<ProductionElement>> trees) {

        for (NaryTree<ProductionElement> tree : trees) {

            for (NaryTreeNode<ProductionElement> child : tree.getRoot().getChildren()) {

                if (child.getElement().getProductionTree() != null) {

                    NaryTree<ProductionElement> subTree = child.getElement().getProductionTree();

                    tree.mergeTree(child, subTree);
                }
            }
        }
    }
}
