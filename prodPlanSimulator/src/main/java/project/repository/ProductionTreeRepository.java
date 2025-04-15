package project.repository;

import project.domain.genericDataStructures.NaryTree;
import project.domain.model.Item;
import project.domain.model.ProductionElement;

import java.util.ArrayList;
import java.util.List;

public class ProductionTreeRepository {
    private List<NaryTree<ProductionElement>> trees;

    public ProductionTreeRepository(List<NaryTree<ProductionElement>> trees) {
        this.trees = trees;
    }

    public ProductionTreeRepository() {
        this.trees = new ArrayList<>();
    }

    public void addProductionTree(NaryTree<ProductionElement> tree) {
        trees.add(tree);
    }

    public void addProductionTrees(List<NaryTree<ProductionElement>> trees) {
        this.trees.addAll(trees);
    }

    public NaryTree<ProductionElement> getTreeByFinalProduct(String finalProduct) {
        for (NaryTree<ProductionElement> tree : trees) {
            if (tree.getRoot().getElement().getItem().getName().equals(finalProduct)) {
                return tree;
            }
            if (tree.getRoot().getElement().getItem().getId().equals(finalProduct)) {
                return tree;
            }
        }
        return null;
    }

    public List<NaryTree<ProductionElement>> getAllTrees() {
        return new ArrayList<>(trees);
    }

    public void clearTrees() {
        trees.clear();
    }

    public boolean isEmpty() {
        return trees.isEmpty();
    }

    public int getTreeCount() {
        return trees.size();
    }
}
