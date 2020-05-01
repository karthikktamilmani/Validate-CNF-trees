import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TreeNode {

    private String data = null;
    private Boolean isTerminal = true;

    private List<TreeNode> children = new ArrayList<>();

    private TreeNode parent = null;

    public TreeNode(String data, boolean isTerminal) {
        this.data = data;
        this.isTerminal = isTerminal;
    }

    public TreeNode addChild(TreeNode child) {
        child.setParent(this);
        this.children.add(child);
        return child;
    }

    public String getData() {
        return data;
    }

    private void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public TreeNode getParent() {
        return parent;
    }

    public Boolean getIsTerminal(){return isTerminal;}

    public List<TreeNode> getChildren(){ return children;}
}


public class ValidateCNF {

    final private static char OPEN_PARANTHESE = '(';
    final private static char CLOSE_PARANTHESE = ')';

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        StringBuilder inputContent = new StringBuilder();
        boolean isValidCNF = true;
        try {
            System.out.println("Input:");
            Scanner myObj = new Scanner(System.in);
            while (myObj.hasNextLine()) {
                inputContent = inputContent.append(myObj.nextLine().strip() + "\n");
            }
            //
            String input = inputContent.toString();
            TreeNode rootNode = null;
            ArrayList<TreeNode> completedNodes = new ArrayList<TreeNode>();
            Pattern terminalPattern = Pattern.compile("[A-Za-z0-9]+");
            for (String eachLine : input.split("[\r\n]+")) {
                if (eachLine.trim().equals("")) {
                    continue;
                }
                if (eachLine.contains("(S")) {
                    // new tree
                    rootNode = null;
                }
                for (String eachNode : eachLine.split("[ \t]+")) {
                    if (eachNode.charAt(0) == OPEN_PARANTHESE) {
                        if (rootNode == null) {
                            rootNode = new TreeNode(eachNode.substring(1), false);
                        } else {
                            TreeNode childNode = new TreeNode(eachNode.substring(1), false);
                            rootNode.addChild(childNode);
                            rootNode = childNode;
                        }
                    } else {
                        Matcher m = terminalPattern.matcher(eachNode);
                        if (m.find()) {
                            String dataValue = m.group();
                            //
                            TreeNode childNode = new TreeNode(dataValue, true);
                            rootNode.addChild(childNode);
                            rootNode = childNode;
                            //
                            eachNode = eachNode.replace(dataValue, "");
                        }
                        //
                        char arr[] = eachNode.toCharArray();
                        for (char c : arr) {
                            if (c == CLOSE_PARANTHESE) {
                                if (!rootNode.getData().equals("S")) {
                                    if (rootNode.getIsTerminal().equals(true)) {
                                        rootNode = rootNode.getParent();
                                        if (rootNode.getData().equals("S")) {
                                            completedNodes.add(rootNode);
                                            rootNode = null;
                                        } else {
                                            rootNode = rootNode.getParent();
                                        }
                                    } else {
                                        rootNode = rootNode.getParent();
                                    }
                                } else {
                                    completedNodes.add(rootNode);
                                    rootNode = null;
                                }
                            }
                        }

                    }

                }

            }
            //
            if (rootNode != null && rootNode.getData().equals("S")) {
                completedNodes.add(rootNode);
                rootNode = null;
            }
            //
            for (TreeNode eachTreeNode : completedNodes) {
                //
                if (!recursiveNodeCheck(eachTreeNode)) {
                    isValidCNF = false;
                    break;
                }
            }
            //
        } catch (Exception e) {
            isValidCNF = false;
        }
        if (isValidCNF) {
            System.out.println("Valid CNF trees.");
        } else {
            System.out.println("Not valid CNF trees.");
        }

    }


    public static boolean recursiveNodeCheck(TreeNode eachTreeNode) throws Exception {
        if (eachTreeNode.getChildren().size() > 0) {
            if (!checkNodeCNF(eachTreeNode)) {
                return false;
            }
            Iterator<TreeNode> childNodesIter = eachTreeNode.getChildren().iterator();
            while (childNodesIter.hasNext()) {
                TreeNode childNode = childNodesIter.next();
                if (!recursiveNodeCheck(childNode)) {
                    return false;
                }
            }

        }
        //
        return true;
    }

    public static boolean checkNodeCNF(TreeNode node) throws Exception {
        if (node.getIsTerminal().equals(true)) {
            if (node.getChildren().size() > 0) {
                return false;
            }
        } else {
            if (node.getChildren().size() > 2) {
                return false;
            } else {
                //
//				List<TreeNode> childNodes = node.getChildren();
                boolean isTerminalExists = false;
                Iterator<TreeNode> childNodesIter = node.getChildren().iterator();
                while (childNodesIter.hasNext()) {
                    TreeNode childNode = childNodesIter.next();
                    if (!isTerminalExists) {
                        isTerminalExists = childNode.getIsTerminal();
                    } else {
                        return false;
                    }
                    //
                }
            }
        }
        return true;
    }

}
