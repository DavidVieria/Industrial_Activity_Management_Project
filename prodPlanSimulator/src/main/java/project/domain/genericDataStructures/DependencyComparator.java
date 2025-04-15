package project.domain.genericDataStructures;

import project.domain.service.ActivityInfo;

import java.util.Comparator;

public class DependencyComparator<K> implements Comparator<K> {

    @Override
    public int compare(K a, K b) {
        // Ensure both objects are instances of NaryTreeNode
        if (a instanceof ActivityInfo && b instanceof ActivityInfo) {
            ActivityInfo activityA = (ActivityInfo) a;
            ActivityInfo activityB = (ActivityInfo) b;

            // Compare the depth levels of the two nodes
            return Integer.compare(activityB.getDependencyLevel(), activityA.getDependencyLevel());
        }

        // Throw an exception if the objects are not of the expected type
        throw new IllegalArgumentException("Objects compared must be instances of ActivityInfo.");
    }
}