package jadx.fxgui.treemodel;

import jadx.api.ResourceFile;
import jadx.fxgui.JadxWrapper;
import jadx.fxgui.utils.Utils;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class JRoot extends JNode {
    private static final long serialVersionUID = 8888495789773527342L;

    private static final Image ROOT_ICON = Utils.openIcon("java_model_obj");

    private final JadxWrapper wrapper;

    private boolean flatPackages = false;

    public JRoot(JadxWrapper wrapper) {
        this.wrapper = wrapper;
        init();
        update();
    }

    public final void update() {
        removeAllChildren();
        add(new JSources(this, wrapper));

        List<JResource> resList = getHierarchyResources(wrapper.getResources());
        for (JResource jRes : resList) {
            jRes.update();
            add(jRes);
        }
    }

    private List<JResource> getHierarchyResources(List<ResourceFile> resources) {
        if (resources.isEmpty()) {
            return Collections.emptyList();
        }
        JResource root = new JResource(null, "Resources", JResource.JResType.ROOT);
        String splitPathStr = Pattern.quote(File.separator);
        for (ResourceFile rf : resources) {
            String[] parts = new File(rf.getName()).getPath().split(splitPathStr);
            JResource curRf = root;
            int count = parts.length;
            for (int i = 0; i < count; i++) {
                String name = parts[i];
                JResource subRF = getResourceByName(curRf, name);
                if (subRF == null) {
                    if (i != count - 1) {
                        subRF = new JResource(null, name, JResource.JResType.DIR);
                    } else {
                        subRF = new JResource(rf, name, JResource.JResType.FILE);
                    }
                    curRf.getFiles().add(subRF);
                }
                curRf = subRF;
            }
        }
        return Collections.singletonList(root);
    }

    private JResource getResourceByName(JResource rf, String name) {
        for (JResource sub : rf.getFiles()) {
            if (sub.getName().equals(name)) {
                return sub;
            }
        }
        return null;
    }

    public JNode searchClassInTree(JNode node) {
        //TODO:
//		Enumeration<?> en = this.breadthFirstEnumeration();
//		while (en.hasMoreElements()) {
//			Object obj = en.nextElement();
//			if (node.equals(obj)) {
//				return (JNode) obj;
//			}
//		}
        return null;
    }

    public boolean isFlatPackages() {
        return flatPackages;
    }

    public void setFlatPackages(boolean flatPackages) {
        if (this.flatPackages != flatPackages) {
            this.flatPackages = flatPackages;
            update();
        }
    }

    @Override
    public Node getIcon() {
        return new ImageView(ROOT_ICON);
    }

    @Override
    public JClass getJParent() {
        return null;
    }

    @Override
    public int getLine() {
        return 0;
    }

    @Override
    public String makeString() {
        File file = wrapper.getOpenFile();
        return file != null ? file.getName() : "File not open";
    }
}
