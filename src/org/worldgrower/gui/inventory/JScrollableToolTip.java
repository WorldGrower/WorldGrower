/*******************************************************************************
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.worldgrower.gui.inventory;

import java.awt.*;
import java.awt.event.*;
  
import javax.swing.*;
  
public class JScrollableToolTip extends JToolTip implements MouseWheelListener {
    private JTextPane textPane;
     
    public JScrollableToolTip(final int width, final int height) {
        setPreferredSize(new Dimension(width, height));
        setLayout(new BorderLayout());
        textPane = new JTextPane();
        textPane.setEditable(true);
        textPane.setContentType("text/html");
         
        LookAndFeel.installColorsAndFont(textPane, 
                "ToolTip.background",
                "ToolTip.foreground",
                "ToolTip.font");
            
        JScrollPane scrollpane = new JScrollPane(textPane);
        scrollpane.setBorder(null);
        scrollpane.getViewport().setOpaque(false);
        add(scrollpane);
    }
     
    @Override
    public void addNotify() {
        super.addNotify();
        JComponent comp = getComponent();
        if (comp != null) {
            comp.addMouseWheelListener(this);
        }
    }
  
    @Override
    public void removeNotify() {
        JComponent comp = getComponent();
        if(comp != null) {
            comp.removeMouseWheelListener(this);
        } 
        super.removeNotify();
    }
     
    public void mouseWheelMoved(final MouseWheelEvent e) {
        JComponent comp = getComponent();
        if(comp != null) {
            textPane.dispatchEvent(new MouseWheelEvent(textPane, 
                    e.getID(), e.getWhen(), e.getModifiers(),
                    0, 0, e.getClickCount(), e.isPopupTrigger(),
                    e.getScrollType(), e.getScrollAmount(), e.getWheelRotation()));
        }
    }
  
    @Override
    public void setTipText(final String tipText) {
        String oldValue = this.textPane.getText();
        textPane.setText(tipText);
        textPane.setCaretPosition(0);
        firePropertyChange("tiptext", oldValue, tipText);
    }
  
    @Override
    public String getTipText() {
        return textPane == null ? "" : textPane.getText();
    }
  
    @Override
    protected String paramString() {
        String tipTextString = (textPane.getText() != null ? textPane.getText() : "");
  
        return super.paramString()
                + ",tipText=" + tipTextString;
    }
}