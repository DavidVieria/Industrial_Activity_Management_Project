package project.controller;

import project.domain.genericDataStructures.NaryTree;
import project.domain.model.ProductionElement;
import project.domain.service.MergeTrees;
import project.repository.ProductionTreeRepository;
import project.repository.Repositories;

public class ProductionTreeController {

    private ProductionTreeRepository productionTreeRepository;
    private NaryTree<ProductionElement> tree;

    public ProductionTreeController(NaryTree<ProductionElement> tree) {
        getProductionTreeRepository();
        this.tree = tree;
    }

    public void mergeTrees() {
        MergeTrees.merge(getProductionTreeRepository().getAllTrees());
    }

    public NaryTree<ProductionElement> getTree() {
        return tree;
    }

    public void setTree(NaryTree<ProductionElement> tree) {
        this.tree = tree;
    }

    public NaryTree<ProductionElement> getTreeByProduct (String finalProduct) {
        return getProductionTreeRepository().getTreeByFinalProduct(finalProduct);
    }

    public ProductionTreeRepository getProductionTreeRepository() {
        if (productionTreeRepository == null) {
            Repositories repositories = Repositories.getInstance();
            productionTreeRepository = repositories.getProductionTreeRepository();
        }
        return productionTreeRepository;
    }

}
