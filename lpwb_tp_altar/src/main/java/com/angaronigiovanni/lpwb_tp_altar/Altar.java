package com.angaronigiovanni.lpwb_tp_altar;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import org.bukkit.Material;

public class Altar {
    List<List<List<Material>>> structure;
    int height;
    int width;
    int depth;
    Sacrifice sacrifice;
    int[] centerPos = {0, 0, 0};

    public Altar(@Nonnull List<List<List<Material>>> structure, Sacrifice sacrifice, int[] centerPos)
    {
        this.structure = structure;
        this.height = structure.size();
        this.depth = structure.get(0).size();
        this.width = structure.get(0).get(0).size();
        this.sacrifice = sacrifice;
        this.centerPos = centerPos;
    }

    public Material getStructureMaterial(int x, int y, int z){
        return this.structure.get(y).get(z).get(x);
    }

    public Material getAltarMaterial() {
        return this.getStructureMaterial(centerPos[1], centerPos[2], centerPos[0]);
    }

    public void rotateStructure() {
        List<List<List<Material>>> rotatedStructure = new ArrayList<>();

        for (List<List<Material>> layer : structure) {
            rotatedStructure.add(rotateMatrix(layer));
        }

        this.structure = rotatedStructure;
    }

    private <T> List<List<T>> rotateMatrix(List<List<T>> matrix) {
        int rows = matrix.size();
        int cols = matrix.get(0).size();

        // Create a List of Lists for the rotated matrix
        List<List<T>> rotatedMatrix = new ArrayList<>();
        for (int i = 0; i < cols; i++) {
            rotatedMatrix.add(new ArrayList<>());
        }

        // Populate the rotated matrix by rearranging elements
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotatedMatrix.get(j).add(matrix.get(i).get(j));
            }
        }

        return rotatedMatrix;
    }

    public String toString() {
        String struct = "";
        for (int i = 0; i < structure.size(); i++) {
            struct += "{\n";
            for (int j = 0; j < structure.get(0).size(); j++) {
                struct += "{\t";
                for (int k = 0; k < structure.get(0).get(0).size(); k++)  {
                    if (structure.get(i).get(j).get(k) != null) 
                        struct += structure.get(i).get(j).get(k).toString() + ", ";
                    else struct += "null, ";
                }
                struct += "\t}";
            }
            struct += "\n}";
        }
        
        return struct;
    }
}
