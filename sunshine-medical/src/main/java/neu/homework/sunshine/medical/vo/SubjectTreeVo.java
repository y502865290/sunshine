package neu.homework.sunshine.medical.vo;

import lombok.Data;
import neu.homework.sunshine.medical.domain.MmsSubject;

import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.List;

@Data
public class SubjectTreeVo {
    @Data
    public static class TreeNode{
        private Long id;
        private Long father;
        private String label;
        private Integer level;
        private List<TreeNode> children;

        public TreeNode(){
            this.children = new ArrayList<>();
        }
        public void setNode(MmsSubject mmsSubject){
            this.id = mmsSubject.getId();
            this.father = mmsSubject.getFather();
            this.label = mmsSubject.getName();
            this.level = mmsSubject.getLevel();
        }

        public void addChild(TreeNode node){
            this.children.add(node);
        }
    }
    private List<TreeNode> data;
}
