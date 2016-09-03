package com.hiep.common.util;

/**
* #Copyright#
* Copyright 2002 Transdyn Controls, Inc. All rights reserved.
* TRANSDYN PROPRIETARY/CONFIDENTIAL.
* Use is subject to license terms.
* #EndCopyright#
*/



import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.table.*;
import javax.swing.text.JTextComponent;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import java.awt.*;
import java.awt.event.KeyListener;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;


/**
 * Static convenience methods for GUIs which eliminate code duplication.
 * <p>
 * Adapted and modified from code available on javapractices.com.
 *
 * @author <a href="http://www.javapractices.com/">javapractices.com</a>
 */
public class UiUtil {

    private static final int STANDARD_BORDER = 5;
    private static final int ONE_SPACE = 5;
    private static final int THREE_SPACES = ONE_SPACE * 3;
    private static final int SIMPLE_FIELD_WIDTH = 50;
    private static final int MAX_LABEL_LENGTH = 150;
    
    private static final int DEFAULT_REDUCED_HORIZONTAL_BUTTON_MARGIN = 5;
    
    private static final int MAXIMUM_MENU_CHARACTER_WIDTH = 50;


    /**
     * Color used to represent a bad value in a component
     */
    public static final Color INVALID_VALUE_COLOR = Color.decode( "#FB983D" );
    
    /**
     * Color used to represent a bad value for a selected element in a component
     */
    public static final Color INVALID_VALUE_COLOR_SELECTED = INVALID_VALUE_COLOR.darker();
    
    private static final String ORIGINAL_OPAQUE_VALUE = "UiUtil.__ORIGINAL_OPAQUE_VALUE";
    
    /**
     * Maintains a flag to indicate the current thread is in the process of determining the
     * initial size of a dialog.  This gives a swing component the opportunity to report
     * an alternate preferred size to be allocated when a dialog is to be displayed than what
     * it normally reports as its preferred size.
     */
    private static final ThreadLocal<Boolean> sizingDialog = new ThreadLocal<Boolean>();
    
    
    public static boolean isSizingDialog() {
        Boolean b =  sizingDialog.get();
        return b == null ? false : b.booleanValue();
    }
    
    public static void startSizingDialog() {
        sizingDialog.set( Boolean.TRUE );
    }
    
    
    public static void endSizingDialog() {
        sizingDialog.remove();
    }
    
    
    /**
     * <code>pack</code>, center, and <code>show</code> a window on the screen.
     *
     * <P>If the size of <code>aWindow</code> exceeds that of the screen,
     * then the size of <code>aWindow</code> is reset to the size of the screen.
     */
    public static void centerAndShow(Window aWindow){
        //note that the order here is important
        aWindow.pack();

        centerOnScreen( aWindow );

        aWindow.setVisible(true);
    }


    public static void centerOnScreen( Window aWindow ) {
        /*
        * If called from outside the event dispatch thread (as is
        * the case upon startup, in the launch thread), then
        * in principle this code is not thread-safe: once pack has
        * been called, the component is realized, and (most) further
        * work on the component should take place in the event-dispatch
        * thread.
        *
        * In practice, it is exceedingly unlikely that this will lead
        * to an error, since invisible components cannot receive events.
        */
        
        Container parentWindow = aWindow.getParent();
        if( parentWindow == null || !parentWindow.isShowing()) {
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension window = aWindow.getSize();
            //ensure that no parts of aWindow will be off-screen
            if (window.height > screen.height) {
                window.height = screen.height;
            }
            if (window.width > screen.width) {
                window.width = screen.width;
            }
            int xCoord = (screen.width/2 - window.width/2);
            int yCoord = (screen.height/2 - window.height/2);

            aWindow.setSize( window );
            aWindow.setLocation( xCoord, yCoord );
            return;
        }
        
        Rectangle parentScreenArea = new Rectangle( 
                parentWindow.getLocationOnScreen(),
                parentWindow.getSize() );
        
       Rectangle availableScreenArea = UiUtil.getCombinedScreenBounds( UiUtil.getScreens( parentScreenArea ), 
                new Rectangle( 0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE ) );
       
       Dimension childSize = aWindow.getSize();
       
       Point newLocation = new Point(
               availableScreenArea.x + ( availableScreenArea.width / 2 ) - ( childSize.width / 2 ),
               availableScreenArea.y + ( availableScreenArea.height / 2 ) - ( childSize.height / 2 ) );
       
       newLocation = restrictChildBoundsToParentMonitors( aWindow, newLocation );
       
       aWindow.setLocation( newLocation );
        
    }


    /**
     * A window is packed, centered with respect to a parent, and then shown.
     *
     * <P>This method is intended for dialogs only.
     *
     * <P>If centering with respect to a parent causes any part of the dialog
     * to be off screen, then the centering is overidden, such that all of the
     * dialog will always appear fully on screen, but it will still appear
     * near the parent.
     *
     * @param aWindow must have non-null result for <code>aWindow.getParent</code>.
     */
    public static void centerOnParentAndShow(Window aWindow){
        aWindow.pack();
        centerOnParent( aWindow );
        aWindow.setVisible(true);
    }


    public static void centerOnParent( Window aWindow ) {
        Container parent = aWindow.getParent();
        if ( null == parent ) {
            centerOnScreen( aWindow );
            return;
        }
        
        Dimension parentSize = parent.getSize();
        Dimension windowSize = aWindow.getSize();


        Point parentLocation = null;
        try {

            parentLocation = parent.getLocationOnScreen();

        } catch ( IllegalComponentStateException e ) {
            // If the parentSize component is not showing on the screen, center
            // the windowSize on the screen instead of on the parentSize
            centerOnScreen( aWindow );
            return;
        }

        int xCoord = parentLocation.x + (parentSize.width/2 - windowSize.width/2);
        int yCoord = parentLocation.y + (parentSize.height/2 - windowSize.height/2);

        Point newLocation = restrictChildBoundsToParentMonitors( aWindow, new Point( xCoord, yCoord ) );
        aWindow.setLocation( newLocation.x, newLocation.y );
    }


    /**
     * Return a border of dimensions recommended by the Java Look and Feel
     * Design Guidelines, suitable for many common cases.
     *
     *<P> Each side of the border has size {@link #STANDARD_BORDER}.
     */
    public static Border getStandardBorder(){
        return BorderFactory.createEmptyBorder(STANDARD_BORDER,
                                               STANDARD_BORDER,
                                               STANDARD_BORDER,
                                               STANDARD_BORDER);
    }

    /**
     * Return text which conforms to the Look and Feel Design Guidelines
     * for the title of a dialog : the application name, a colon, then
     * the name of the specific dialog.
     *
     *
     */
    public static String getDialogTitle(String appName, String aSpecificDialogName){
        //Args.checkForContent(aSpecificDialogName);

        StringBuffer result = new StringBuffer(appName);
        result.append(": ");
        result.append(aSpecificDialogName);
        return result.toString();
    }

    /**
     * Make a horizontal row of buttons of equal size, whch are equally spaced,
     * and aligned on the right.
     *
     * <P>The returned component has border spacing only on the top (of the size
     * recommended by the Look and Feel Design Guidelines).
     * All other spacing must be applied elsewhere ; usually, this will only mean
     * that the dialog's top-level panel should use {@link #getStandardBorder}.
     *
     * @param aButtons contains <code>JButton</code> objects.
     * @param alignment either LEFT, CENTER, or RIGHT alignment as defined in
     * SwingConstants.
     */
    public static JComponent getCommandRow(java.util.List<? extends JButton> aButtons, int alignment ){
        equalizeSizes( aButtons );
        JPanel panel = new JPanel();
        LayoutManager layout = new BoxLayout(panel, BoxLayout.X_AXIS);
        panel.setLayout( layout );
        panel.setBorder(  BorderFactory.createEmptyBorder(THREE_SPACES, 0, THREE_SPACES, 0) );

        // Add horizontal glue before buttons if center or right aligned
        if( alignment == SwingConstants.CENTER ||
            alignment == SwingConstants.RIGHT ) {
            panel.add( Box.createHorizontalGlue() );
        }

        Iterator<? extends JButton> buttonsIter = aButtons.iterator();
        while ( buttonsIter.hasNext() ) {
            panel.add( buttonsIter.next() );
            if ( buttonsIter.hasNext() ) {
                panel.add( Box.createHorizontalStrut(ONE_SPACE) );
            }
        }

        // Add horizontal glue after buttons if center or left aligned
        if( alignment == SwingConstants.CENTER ||
            alignment == SwingConstants.LEFT ) {
            panel.add( Box.createHorizontalGlue() );
        }


        return panel;
    }

    
    /**
     * Make a vertical row of buttons of equal size, whch are equally spaced,
     * and aligned on the right.
     *
     * <P>The returned component has border spacing only on the left (of the size
     * recommended by the Look and Feel Design Guidelines).
     * All other spacing must be applied elsewhere ; usually, this will only mean
     * that the dialog's top-level panel should use {@link #getStandardBorder}.
     *
     * @param aButtons contains <code>JButton</code> objects.
     */
    public static JComponent getCommandColumn( java.util.List<? extends JButton> aButtons ){
        equalizeSizes( aButtons );
        JPanel panel = new JPanel();
        LayoutManager layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout( layout );
        panel.setBorder(  BorderFactory.createEmptyBorder(0,THREE_SPACES, 0,THREE_SPACES) );
        Iterator<? extends JButton> buttonsIter = aButtons.iterator();
        while ( buttonsIter.hasNext() ) {
            panel.add( buttonsIter.next() );
            if ( buttonsIter.hasNext() ) {
                panel.add( Box.createVerticalStrut(ONE_SPACE) );
            }
        }
        panel.add( Box.createVerticalGlue() );
        return panel;
    }


    /**
     * Return a <code>Dimension</code> whose size is defined not in terms of pixels,
     * but in terms of a given percent of the screen's width and height.
     *
     *<P> Use to set the preferred size of a component to a certain
     * percentage of the screen.
     *
     * @param aPercentWidth percentage width of the screen, in range <code>1..100</code>.
     * @param aPercentHeight percentage height of the screen, in range <code>1..100</code>.
     */
    public static final Dimension getDimensionFromPercent(int aPercentWidth, int aPercentHeight){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return calcDimensionFromPercent(screenSize, aPercentWidth, aPercentHeight);
    }

    /**
     * Sets the items in <code>aComponents</code> to the same size.
     *
     * Sets each component's preferred and maximum sizes.
     * The actual size is determined by the layout manager, whcih adjusts
     * for locale-specific strings and customized fonts. (See this
     * <a href="http://java.sun.com/products/jlf/ed2/samcode/prefere.html">Sun doc</a>
     * for more information.)
     *
     * @param aComponents contains <code>JComponent</code> objects.
     */
    public static void equalizeSizes( JComponent[] components ) {
        Dimension targetSize = new Dimension(0,0);
        for( JComponent comp : components ) {
            Dimension compSize = comp.getPreferredSize();
            double width = Math.max(targetSize.getWidth(), compSize.getWidth());
            double height = Math.max(targetSize.getHeight(), compSize.getHeight());
            targetSize.setSize(width, height);
        }
        setSizes(components, targetSize);
    }
    
    public static void equalizeSizes( java.util.List<? extends JComponent> aComponents ) {
        equalizeSizes( aComponents.toArray( new JComponent[aComponents.size()] ) );
    }

    /**
     * Create a pair of components, a <code>JLabel</code> and an associated
     * <code>JTextField</code>, as is typically used for user input.
     *
     *<P>The <code>JLabel</code> appears on the left, and the <code>JTextField</code>
     * appears on the same  row, just to the right of the <code>JLabel</code>.
     * The <code>JLabel</code> has a mnemonic which forwards focus to the
     * <code>JTextField</code> when activated.
     *
     * @param aContainer holds the pair of components.
     * @param aName text of the <code>JLabel</code> component.
     * @param aInitialValue possibly-null initial value to appear
     * in the <code>JTextField</code>; if <code>null</code>, then
     * <code>JTextField</code> will be blank.
     * @param aMnemonic <code>KeyEvent</code> field, used as the mnemonic for
     * the <code>JLabel</code>.
     * @param aConstraints applied to the <code>JLabel</code>; the corresponding
     * constraints for the <code>JTextField</code> are the same as
     * <code>aConstraints</code>, except for <code>gridx</code> being incremented by one;
     * in addition, if <code>aConstraints</code> has <code>weightx=0</code> (the default),
     * then the entry field will receive <code>weightx=1.0</code> (entry field gets more
     * horizontal space upon resize).
     * @param aTooltip possibly-null text displayed as tool tip for the
     * <code>JTextField</code> ; if <code>null</code>, the tool tip is turned off.
     * @return the user input <code>JTextField</code>.
     */
    public static JTextField addSimpleEntryField(Container aContainer,
                                                 String aName,
                                                 String aInitialValue,
                                                 int aMnemonic,
                                                 GridBagConstraints aConstraints,
                                                 String aTooltip){
        //Args.checkForNull(aName);

        JLabel label = new JLabel(aName);
        label.setDisplayedMnemonic(aMnemonic);
        aContainer.add( label, aConstraints );

        JTextField result = new JTextField(SIMPLE_FIELD_WIDTH);
        label.setLabelFor(result);
        result.setToolTipText(aTooltip);
        if ( aInitialValue != null) {
            result.setText(aInitialValue);
        }
        aConstraints.gridx = ++aConstraints.gridx;
        if (aConstraints.weightx == 0.0){
            aConstraints.weightx = 1.0;
        }
        aContainer.add( result, aConstraints );
        return result;
    }

    /**
     * Return a set of constraints with convenient default values.
     *
     *<P>Return constraints with these values :
     *<ul>
     * <li> <code>gridx, gridy</code> - set to <code>aX, aY</code>
     * <li> <code>anchor - GridBagConstraints.WEST</code>
     * <li> <code>insets - Insets(0,0,0, ONE_SPACE)</code>
     *</ul>
     *
     *<P> All other items simply take their default values :
     *<ul>
     * <li> <code>fill - GridBagConstraints.NONE</code>
     * <li> <code>gridwidth, gridheight - 0, 0</code>
     * <li> <code>weightx , weighty - 0, 0</code>
     * <li> <code>ipadx, ipady - 0, 0</code>
     *</ul>
     *
     * <P>The caller is free to change the returned constraints, to customize for
     * their particular needs.
     *
     * @param aY in range <code>0..10</code>.
     * @param aX in range <code>0..10</code>.
     */
    public static GridBagConstraints getConstraints(int aY, int aX){
        GridBagConstraints result = new GridBagConstraints();
        result.gridy = aY;
        result.gridx = aX;
        result.anchor = GridBagConstraints.WEST;
        result.insets = new Insets(0,0,0,ONE_SPACE);
        return result;
    }

    /**
     * Return {@link #getConstraints}, with the addition of setting
     * <code>gridwidth</code> to <code>aWidth</code>, and setting
     * <code>gridheight</code> to <code>aHeight</code>.
     *
     * <P>The caller is free to change the returned constraints, to customize for
     * their particular needs.
     *
     * @param aY in range <code>0..10</code>.
     * @param aX in range <code>0..10</code>.
     * @param aWidth in range <code>1..10</code>.
     * @param aHeight in range <code>1..10</code>.
     */
    public static GridBagConstraints getConstraints(int aY, int aX, int aWidth, int aHeight){
        GridBagConstraints result = getConstraints(aY, aX);
        result.gridheight = aHeight;
        result.gridwidth = aWidth;
        return result;
    }

    /**
     * Create a pair of <code>JLabel</code> components, as is typically needed
     * for display of a name-value pair.
     *
     * <P>The name appears on the left, and the value appears on the right,
     * all on the same row. A colon and an empty space are appended to the name.
     *
     * <P> If the the length of "value" label is greater than
     * {@link #MAX_LABEL_LENGTH}, then the text is truncated, an ellipsis
     * is placed at its end, and the full text is placed in a tooltip.
     *
     * @param aContainer holds the pair of components.
     * @param aName text of the name <code>JLabel</code>.
     * @param aValue possibly-null ; if null, then an empty <code>String</code>
     * is used for the value; otherwise <code>Object.toString</code> is used.
     * @param aConstraints for the name <code>JLabel</code>; the corresponding
     * constraints for the value <code>JLabel</code> are mostly taken from
     * <code>aConstraints</code>, except for <code>gridx</code> being incremented by one
     * (<code>weightx</code> may differ as well - see <code>aWeightOnDisplay</code>.)
     * @param aWeightOnDisplay if true, then set <code>weightx</code> for the value
     * field to 1.0 (to give it more horizontal space upon resize).
     * @return the <code>JLabel</code> for the value (which is usually variable).
     */
    public static JLabel addSimpleDisplayField(Container aContainer,
                                               String aName,
                                               Object aValue,
                                               GridBagConstraints aConstraints,
                                               boolean aWeightOnDisplay){
        StringBuffer formattedName = new StringBuffer(aName);
        formattedName.append(": ");
        JLabel name = new JLabel( formattedName.toString() );
        aContainer.add( name, aConstraints );

        String valueText = (aValue != null? aValue.toString() : StringUtils.EMPTY_STRING);
        JLabel value = new JLabel(valueText);
        truncateLabelIfLong(value);
        aConstraints.gridx = ++aConstraints.gridx;
        if (aWeightOnDisplay){
            aConstraints.weightx = 1.0;
        }
        aContainer.add( value, aConstraints );

        return value;
    }

    /**
     * Adds "glue" (an empty component with desired resizing behavior) to the
     * bottom row of a <code>GridBagLayout</code> of components. When resized,
     * this glue will take up extra vertical space.
     * 
     * <P>
     * This method is especially useful for text data presented in a listing or
     * tabular form. Such components naturally resize horizontally, while their
     * vertical resizing should often be absent. If such a listing is resized
     * vertically, then this glue can take up the remaining vertical space,
     * keeping the text at the top.
     * 
     * @param aPanel uses <code>GridBagLayout</code>, and contains components
     *        whose <code>weighty</code> values are all 0.0 (the default).
     * @param aLastRowIdx index of the last row of components, in which the glue
     *        will be placed.
     */
    public static void addVerticalGridGlue(JPanel aPanel, int aLastRowIdx) {
        GridBagConstraints glueConstraints = UiUtil.getConstraints(aLastRowIdx,0);
        glueConstraints.weighty = 1.0;
        glueConstraints.fill = GridBagConstraints.VERTICAL;
        aPanel.add( new JLabel(), glueConstraints );
    }

    /**
     * Return a <code>String</code>, suitable for presentation to the end user,
     * representing a percentage having two decimal places, using the default locale.
     *
     * <P>An example return value is "5.15%". The intent of this method is to
     * provide a standard representation and number of decimals for the entire
     * application. If a different number of decimal places is required, then
     * the caller should use <code>NumberFormat</code> instead.
     */
    public static String getLocalizedPercent( Number aNumber ){
        NumberFormat localFormatter = NumberFormat.getPercentInstance();
        localFormatter.setMinimumFractionDigits(2);
        return localFormatter.format( aNumber.doubleValue() );
    }

    /**
     * Return a <code>String</code>, suitable for presentation to the end user,
     * representing an integral number with no decimal places, using the default
     * locale.
     *
     * <P>An example return value is "8,000". The intent of this method is to
     * provide a standard representation of integers for the entire
     * application.
     */
    public static String getLocalizedInteger( Number aNumber ) {
        NumberFormat localFormatter = NumberFormat.getNumberInstance();
        return localFormatter.format( aNumber.intValue() );
    }

    /**
     * Return a <code>String</code>, suitable for presentation to the end user,
     * representing a date in <code>DateFormat.SHORT</code> and the default locale.
     */
    public static String getLocalizedTime(Date aDate){
        DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        return dateFormat.format(aDate);
    }

    /**
     * Make the sytem emit a beep.
     *
     * <P>May not beep unless the speakers are turned on, so this cannot
     * be guaranteed to work.
     */
    public static void beep(){
        Toolkit.getDefaultToolkit().beep();
    }

    /**
     * An alternative to multi-line labels, for the presentation of
     * several lines of text, and for which the line breaks are determined
     * solely by the widget.
     */
    public static JTextArea getStandardTextArea(String aText){
        //Args.checkForContent(aText);
        if ( aText.indexOf(StringUtils.NEW_LINE) != -1 ){
            throw new IllegalArgumentException("Must not contain new line characters: " + aText);
        }

        JTextArea result = new JTextArea(aText);
        result.setEditable(false);
        result.setWrapStyleWord(true);
        result.setLineWrap(true);
        result.setMargin( new Insets(0,ONE_SPACE,0,ONE_SPACE) );
        //this is a bit hacky: the desired color is "secondary3", but cannot see how
        //to reference it directly; hence, an element which uses secondary3 is used instead.
        result.setBackground( MetalLookAndFeel.getMenuBackground() );

        return result;
    }

    /**
     * An alternative to multi-line labels, for the presentation of
     * several lines of text, and for which line breaks are determined
     * solely by <code>aText</code>, and not by the widget.
     *
     * {@link javax.swing.plaf.metal.MetalLookAndFeel#getMenuBackground}.
     */
    public static JTextArea getStandardTextAreaHardNewLines(String aText){
        //Args.checkForContent(aText);

        JTextArea result = new JTextArea(aText);
        result.setEditable(false);
        result.setMargin( new Insets(0,ONE_SPACE,0,ONE_SPACE) );
        result.setBackground( MetalLookAndFeel.getMenuBackground() );
        return result;
    }


    /**
     * Imposes a uniform horizontal alignment on all items in a container.
     *
     *<P> Intended especially for <code>BoxLayout</code>, where all components need
     * to share the same alignment in order for display to be reasonable.
     * (Indeed, this method may only work for <code>BoxLayout</code>, since apparently
     * it is the only layout to use <code>setAlignmentX, setAlignmentY</code>.)
     *
     * @param aContainer contains only <code>JComponent</code> objects.
     */
    public static void alignAllX(Container aContainer, UiUtil.AlignX aAlignment){
        for( Component comp : aContainer.getComponents() ) {
            ((JComponent)comp).setAlignmentX( aAlignment.getValue() );
        }
    }

    /**
     * Type-safe enumeration for horizontal alignment.
     */
    public static final class AlignX {
        public static final AlignX LEFT = new AlignX(Component.LEFT_ALIGNMENT);
        public static final AlignX CENTER = new AlignX(Component.CENTER_ALIGNMENT);
        public static final AlignX RIGHT = new AlignX(Component.RIGHT_ALIGNMENT);
        float getValue(){
            return fValue;
        }
        private final float fValue;
        private AlignX( float aValue){
            fValue = aValue;
        }
    }

    /**
     * Imposes a uniform vertical alignment on all items in a container.
     *
     *<P> Intended especially for <code>BoxLayout</code>, where all components need
     * to share the same alignment in order for display to be reasonable.
     * (Indeed, this method may only work for <code>BoxLayout</code>, since apparently
     * it is the only layout to use <code>setAlignmentX, setAlignmentY</code>.)
     *
     * @param aContainer contains only <code>JComponent</code> objects.
     */
    public static void alignAllY(Container aContainer, UiUtil.AlignY aAlignment){
        for( Component comp : aContainer.getComponents() ) {
            ((JComponent)comp).setAlignmentY( aAlignment.getValue() );
        }
    }

    /**
     * Type-safe enumeration vertical alignment.
     */
    public static final class AlignY {
        public static final AlignY TOP = new AlignY(Component.TOP_ALIGNMENT);
        public static final AlignY CENTER = new AlignY(Component.CENTER_ALIGNMENT);
        public static final AlignY BOTTOM = new AlignY(Component.BOTTOM_ALIGNMENT);
        float getValue(){
            return fValue;
        }
        private final float fValue;
        private AlignY( float aValue){
            fValue = aValue;
        }
    }

    /**
     * Ensure that <code>aRootPane</code> has no default button associated with it.
     *
     * <P>Intended mainly for dialogs where the user is confirming a delete action.
     * In this case, an explicit Yes or No is preferred, with no default action being
     * taken when the user hits the Enter key.
     */
    public static void noDefaultButton(JRootPane aRootPane){
        aRootPane.setDefaultButton(null);
    }

    // PRIVATE //


    private  static void setSizes( JComponent[] aComponents, Dimension aDimension ){
        for( JComponent comp : aComponents ) {
            comp.setPreferredSize( (Dimension)aDimension.clone() );
            comp.setMaximumSize( (Dimension)aDimension.clone() );
        }
    }
    
    private  static Dimension calcDimensionFromPercent( Dimension aSourceDimension,
                                                        int aPercentWidth,
                                                        int aPercentHeight){
        int width = aSourceDimension.width * aPercentWidth/100;
        int height = aSourceDimension.height * aPercentHeight/100;
        return new Dimension(width, height);
    }

    /**
     * If aLabel has text which is longer than MAX_LABEL_LENGTH, then truncate
     * the label text and place an ellipsis at the end; the original text is placed
     * in a tooltip.
     *
     * This is particularly useful for displaying file names, whose length
     * can vary widely between deployments.
     */
    private  static void truncateLabelIfLong(JLabel aLabel){
        truncateLabelIfLong( aLabel, MAX_LABEL_LENGTH );
    }
    
    
    private  static void truncateLabelIfLong( JLabel aLabel, int maxLength ){
        String originalText = aLabel.getText();
        if (originalText.length() > maxLength){
            if( aLabel.getToolTipText() == null ) {
                aLabel.setToolTipText( originalText );
            }
            String truncatedText = originalText.substring(0, maxLength)
                    + StringUtils.ELLIPSIS;
            aLabel.setText(truncatedText);
        }
    }


    private  static void truncateMenuItemIfLong( JMenuItem menuItem, int maxLength ){
        String originalText = menuItem.getText();
        if (originalText.length() > maxLength){
            if( menuItem.getToolTipText() == null ) {
                menuItem.setToolTipText( originalText );
            }
            String truncatedText = originalText.substring(0, maxLength)
                    + StringUtils.ELLIPSIS;
            menuItem.setText(truncatedText);
        }
    }


    public static void addBoldToFont( Component comp ) {
        Font originalFont = comp.getFont();
        int fontStyle = originalFont.getStyle() | Font.BOLD;
        comp.setFont( new Font( originalFont.getFamily(),
                                fontStyle, originalFont.getSize() ) );
    }


    public static void removeBoldFromFont( Component comp ) {
        Font originalFont = comp.getFont();
        int fontStyle = originalFont.getStyle() & (~Font.BOLD);
        comp.setFont( new Font( originalFont.getFamily(),
                                fontStyle, originalFont.getSize() ) );
    }


    public static void reduceFont( Component comp ) {
        Font originalFont = comp.getFont();
        int fontSize = originalFont.getSize() - 2;
        comp.setFont( new Font( originalFont.getFamily(),
                                originalFont.getStyle(), fontSize ) );
    }


    /**
     * Takes the currently entered text in the combo box and commits
     * it to the underlying ComboBoxModel such that a subsequent call
     * to getSelectedItem() will be the last edited text.
     * <p>
     * Normally this is not necessary as this is done automatically
     * when the combo loses focus or when the drop down menu is used.
     * However, in the case where the user enters text and the
     * combo never loses focus because the component is swapped out
     * for another (e.g. when the component is a customizer that is
     * swapped for another customizer using, for example, the
     * Dynac TreeEditorDialog).
     *
     * @param comboBox
     */
//    public static void stopEditing( JComboBox comboBox ) {
//
//        //
//        // The following block of code duplicates the same functionality
//        // done when the JCombo looses focus
//        //
//        Object item = comboBox.getEditor().getItem();
//
//        if( !ObjectUtils.nullSafeEquals(
//                item, comboBox.getSelectedItem() ) ) {
//            comboBox.actionPerformed (
//                    new ActionEvent( comboBox.getEditor(),
//                                     0,
//                                     "",
//                                     EventQueue.getMostRecentEventTime(),
//                                     0 ) );
//        }
//
//    }


    /**
     * Set the number of columns needed in a JTextField to display
     * the given string.
     *
     * @param textField
     * @param text
     * @param minimumColumnCount
     * @param maximumColumnCount
     * @param onlyAllowGrowth
     */
    public static synchronized void setTextFieldColumns(
            final JTextField textField,
            final String text,
            final int minimumColumnCount,
            final int maximumColumnCount,
            final boolean onlyAllowGrowth ) {
        int columns = getTextFieldColumns(
                textField,
                text,
                minimumColumnCount,
                maximumColumnCount,
                onlyAllowGrowth );
        textField.setColumns( columns );
        textField.setMinimumSize( textField.getPreferredSize() );
    }


    /**
     * Get the number of columns needed in a JTextField to display
     * the given string.
     *
     * @param textField
     * @param text
     * @param minimumColumnCount
     * @param maximumColumnCount
     * @param onlyAllowGrowth
     * @return
     */
    public static synchronized int getTextFieldColumns(
            final JTextField textField,
            final String text,
            final int minimumColumnCount,
            final int maximumColumnCount,
            final boolean onlyAllowGrowth ) {

        if( minimumColumnCount < 0 ) {
            throw new IllegalArgumentException( "minumumColumnCount < 0" );
        }

        if( maximumColumnCount < 0 ) {
            throw new IllegalArgumentException( "maximumColumnCount < 0" );
        }

        if( minimumColumnCount > maximumColumnCount && maximumColumnCount > 0 ) {
            throw new IllegalArgumentException( "minimumColumnCount > maximumColumnCount" );
        }

        if( text == null ) {
            return minimumColumnCount;
        }

        // Calculate how wide a single column
        double oneColumnWidth = getWidthOfOneColumn( textField );

        // get the font metrics
        final FontMetrics fm = textField.getFontMetrics( textField.getFont() );
        double textWidth = (double)SwingUtilities.computeStringWidth( fm, text );

        int width = (int)Math.ceil( textWidth / oneColumnWidth );

        if( maximumColumnCount > 0 && width > maximumColumnCount ) {
            width = maximumColumnCount;
        }

        if( width < minimumColumnCount ) {
            width = minimumColumnCount;
        }

        if( onlyAllowGrowth && width < textField.getColumns() ) {
            width = textField.getColumns();
        }

        return width;

    }


    public static int getWidthOfOneColumn( JTextField textField ) {

        int saveColumns = textField.getColumns();

        // Calculate how wide a single column is by detecting the change
        // in width from 1 to 2 columns
        textField.setColumns(1);
        int beginWidth = textField.getPreferredSize().width;
        textField.setColumns(2);

        int oneColumnWidth = textField.getPreferredSize().width - beginWidth;

        // Restore original column count
        textField.setColumns( saveColumns );

        return oneColumnWidth;

    }


    /**
     * Sets the text into the given component.  If the length of the
     * text is longer than the component can fit, then the text is
     * truncated and an ellipsis (...) is appended.
     *
     * @param label
     * @param text
     */
    public  static void setTruncatedText( JLabel label, String text ) {

        // If not text, keep it simple.
        if( !StringUtils.hasText( text ) ) {
            label.setText( StringUtils.EMPTY_STRING );
            return;
        }

        String labelText = text;

        // Does text already fit?

        final FontMetrics fm = label.getFontMetrics( label.getFont() );

        int totalAvailableWidth = label.getWidth();

        int requiredWidth = SwingUtilities.computeStringWidth(
                fm, text );

        if( requiredWidth > totalAvailableWidth ) {

            // Assume the worst.
            labelText = StringUtils.ELLIPSIS;

            // calculate width of elipse
            int elipseWidth = SwingUtilities.computeStringWidth(
                    fm, StringUtils.ELLIPSIS );

            // remainder is available for text
            int remainingWidth = totalAvailableWidth - elipseWidth;

            if( remainingWidth > 0 ) {
                // loop from end of string toward begining until the width
                // fits within the reamining width.
                String subString;
                for( int i=text.length(); i>0; i-- ) {
                    subString = text.substring( 0, i );
                    if( SwingUtilities.computeStringWidth( fm, subString ) <= remainingWidth ) {
                        labelText = subString + StringUtils.ELLIPSIS;
                        break;
                    }
                }
            }

        }

        label.setText( labelText );
        label.setToolTipText( text );

    }


    /**
     * Finds a child component within the given parent Component's hierachy
     * based on the given Predicate condition.  If the parent itself satisfies
     * the Predicate it will be returned.
     *
     * @param parent
     * @param predicate
     * @return
     */
//    public static Component findComponentInHierarchy( Component parent, Predicate predicate ) {
//
//        if( predicate.evaluate( parent ) ) {
//            return parent;
//        }
//
//        if( parent instanceof Container ) {
//
//            Component[] components = ((Container)parent).getComponents();
//            if( components != null ) {
//                Component found;
//                for( Component c : components ) {
//                    found = findComponentInHierarchy( c, predicate );
//                    if( found != null ) {
//                        return found;
//                    }
//                }
//            }
//        }
//
//        return null;
//
//    }

    public static final int sizeAllColumnsToFitData( JTable table, boolean createDefaultColumnsFromModel ) {        
        if( createDefaultColumnsFromModel ) {
            table.createDefaultColumnsFromModel();
        }
        
        TableColumnModel columnModel = table.getTableHeader().getColumnModel();
        
        int totalSize = 0;
        
        // Loop through all columns in the table
        int columnCount = columnModel.getColumnCount();
        for ( int col = 0; col < columnCount; col++ ) {
            totalSize += sizeColumnToFitData( table, columnModel.getColumn( col ) );   
        } 
        
        return totalSize;
    }
    
    public static int getPreferredTableWidth( JTable table, boolean createDefaultColumnsFromModel ) {
        if( createDefaultColumnsFromModel ) {
            table.createDefaultColumnsFromModel();
        }
        
        TableColumnModel columnModel = table.getTableHeader().getColumnModel();
        
        // Loop through all columns in the table
        int totalSize = 0;
        int columnCount = columnModel.getColumnCount();
        for ( int col = 0; col < columnCount; col++ ) {
            totalSize += getPreferredWidthForColumn( table, columnModel.getColumn( col ) );   
        }
        
        return totalSize;
    }
    
    public static int getPreferredWidthForColumn( JTable table, TableColumn column ) {
        if ( column == null ) {
            return 0; // not a valid column skip
        }

        TableModel model = table.getModel();

        // Get the column header value (e.g. label displayed in header for this column).
        Object headerValue = column.getHeaderValue();

        // Translate to the model index
        int modelColumnIndex = column.getModelIndex();

        // Get the renderer for the column header.
        TableCellRenderer headerRenderer = column.getHeaderRenderer();

        if ( headerRenderer == null ) {
            headerRenderer = table.getTableHeader().getDefaultRenderer();
            if ( headerRenderer == null ) {
                headerRenderer = new DefaultTableCellRenderer();
            }
        }

        // Get the swing component for the renderer to use as a testbed
        Component headerRendererComponent =
                headerRenderer.getTableCellRendererComponent(
                        table, headerValue, true, true, -1, modelColumnIndex );

        // Save the column header's preferred width  
        int maxColumnWidth = headerRendererComponent.getPreferredSize().width;

        // Step through all model's rows and find the preferred width of each row for this column
        // Update maxColumnWidth for the widest one (if any are wider the the header's preferred width).
        TableCellRenderer cellRenderer = table.getCellRenderer( 0, modelColumnIndex );
        int rowCount = model.getRowCount();
        for ( int row = 0; row < rowCount; row++ ) {
            Object value = model.getValueAt( row, modelColumnIndex );
            Component cellRendererComponent = cellRenderer.getTableCellRendererComponent(
                        table, value, true, true, row, modelColumnIndex );

            int cellWidth = cellRendererComponent.getPreferredSize().width;

            if ( cellWidth > maxColumnWidth ) {
                maxColumnWidth = cellWidth;
            }
        }

        // Set the column width to fit the maximum
        Dimension cellSpacing = table.getIntercellSpacing();
        int width = maxColumnWidth + (cellSpacing != null ? cellSpacing.width : 1);
        width += 4;  // Give just a bit extra
        return width;
    }

    public static final int sizeColumnToFitData( JTable table, TableColumn column ) {
        if ( column == null ) {
            return 0; // not a valid column skip
        }

        TableModel model = table.getModel();

        // Get the column header value (e.g. label displayed in header for this column).
        Object headerValue = column.getHeaderValue();

        // Translate to the model index
        int modelColumnIndex = column.getModelIndex();

        // Get the renderer for the column header.
        TableCellRenderer headerRenderer = column.getHeaderRenderer();

        if ( headerRenderer == null ) {
            headerRenderer = table.getTableHeader().getDefaultRenderer();
            if ( headerRenderer == null ) {
                headerRenderer = new DefaultTableCellRenderer();
            }
        }

        // Get the swing component for the renderer to use as a testbed
        Component headerRendererComponent =
                headerRenderer.getTableCellRendererComponent(
                        table, headerValue, true, true, -1, modelColumnIndex );

        // Save the column header's preferred width  
        int maxColumnWidth = headerRendererComponent.getPreferredSize().width;

        // Step through all model's rows and find the preferred width of each row for this column
        // Update maxColumnWidth for the widest one (if any are wider the the header's preferred width).
        TableCellRenderer cellRenderer = table.getCellRenderer( 0, modelColumnIndex );
        int rowCount = model.getRowCount();
        for ( int row = 0; row < rowCount; row++ ) {
            Object value = model.getValueAt( row, modelColumnIndex );
            if(cellRenderer == null){
                cellRenderer = new DefaultTableCellRenderer();
            }
            Component cellRendererComponent = cellRenderer.getTableCellRendererComponent(
                        table, value, true, true, row, modelColumnIndex );

            int cellWidth = cellRendererComponent.getPreferredSize().width;

            if ( cellWidth > maxColumnWidth ) {
                maxColumnWidth = cellWidth;
            }
        }

        // Set the column width to fit the maximum
        Dimension cellSpacing = table.getIntercellSpacing();
        int width = maxColumnWidth + (cellSpacing != null ? cellSpacing.width : 1);
        width += 4;  // Give just a bit extra
        column.setPreferredWidth( width );
        return width;
    }


    /**
     * Sets a components valid state.
     * <p>
     * Uses a system-wide standard way to represent a component in an invalid state.
     * (typically component's background is red).
     * 
     * @param component The component to set the valid state on.
     * @param valid True if component is valid, false if not.
     */
    public static final void setComponentValid( JComponent component, boolean valid ) {
        setComponentValid( component, valid, false );
    }
    

    /**
     * Sets a components valid state.
     * <p>
     * Uses a system-wide standard way to represent a component in an invalid state.
     * (typically component's background is red).
     * 
     * @param component The component to set the valid state on.
     * @param valid True if component is valid, false if not.
     * @param validIfDisabled If true, component will show as valid (no orange background) if the component is
     * disabled. 
     */
    public static final void setComponentValid( final JComponent component, boolean valid, boolean validIfDisabled ) {
        
        if( validIfDisabled && !component.isEnabled() ) {
            valid = true;
        }
        
        if( component instanceof JTextComponent ) {
            setTextComponentValid( (JTextComponent)component, valid );
        } else if( component instanceof JSpinner ) {
            setSpinnerValid( (JSpinner)component, valid );
        } else if( component instanceof JComboBox ) {
            setComboBoxValid( (JComboBox)component, valid );
        } else if( component instanceof JLabel ) {
            setLabelValid( (JLabel)component, valid );
        } else if( component instanceof JPanel ) {
            setPanelValid( (JPanel)component, valid );
        } else {
            throw new IllegalArgumentException( "Unsupported component type: " + component.getClass() );
        }
        
    }


    /**
     * Sets a label's valid state.
     * 
     * @param label The label
     * @param valid True if component is valid.
     */
    private static final void setLabelValid( JLabel label, boolean valid ) {

        // if first time visiting this label, save the opaque value
        Boolean originalOpaqueValue = (Boolean)label.getClientProperty( ORIGINAL_OPAQUE_VALUE );
        if( originalOpaqueValue == null ) {
            originalOpaqueValue = Boolean.valueOf( label.isOpaque() );
            label.putClientProperty( ORIGINAL_OPAQUE_VALUE, originalOpaqueValue );
        }
        
        setLabelValid( label, valid, false, UIManager.getColor( "Label.background" ), null );
        
        if( valid ) {
            label.setOpaque( originalOpaqueValue.booleanValue() );
        } else {
            label.setOpaque( true );
        }
        
    }
    
    
    /**
     * Sets a <code>DefaultListCellRenderer</code>'s valid state. 
     * 
     * @param listCellRenderer The list cell renderer
     * @param valid The valid state
     * @param selected The cell's selected state.
     */
    public static final void setListCellRendererValid( DefaultListCellRenderer listCellRenderer, boolean valid, boolean selected ) {
        setLabelValid( listCellRenderer, valid, selected, 
                UIManager.getColor( "List.background" ),
                UIManager.getColor( "List.selectionBackground" ) );
    }
    
    
    /**
     * Sets a <code>DefaultListCellRenderer</code>'s valid state. 
     * 
     * @param listCellRenderer The list cell renderer
     * @param valid The valid state
     * @param selected The cell's selected state.
     */
    public static final void setTableCellRendererValid( DefaultTableCellRenderer tableCellRenderer, boolean valid, boolean selected ) {
        setLabelValid( tableCellRenderer, valid, selected, 
                UIManager.getColor( "Table.background" ),
                UIManager.getColor( "Table.selectionBackground" ) );
    }
    
    
    /**
     * Sets a <code>DefaultListCellRenderer</code>'s valid state when it is being displayed in a combo
     * box (as opposed to a JList). 
     * 
     * @param listCellRenderer The list cell renderer
     * @param valid The valid state
     * @param selected The cell's selected state.
     */
    public static final void setComboBoxRendererValid( DefaultListCellRenderer listCellRenderer, boolean valid, boolean selected ) {
        setLabelValid( listCellRenderer, valid, selected, 
                UIManager.getColor( "ComboBox.background" ),
                UIManager.getColor( "ComboBox.selectionBackground" ) );
    }
    
    
    /**
     * Sets a label's valid state.
     * 
     * @param label The label
     * @param valid The valid state.
     * @param selected The selected state (if list cell renderer)
     * @param validColor The color to use when valid and unselected.
     * @param validSelectedColor The color to use when valid and selected.
     */
    private static final void setLabelValid( JLabel label, boolean valid, boolean selected, Color validColor, Color validSelectedColor ) {
        
        Color color = null;
        if( valid ) {
            if( selected ) {
                color = validSelectedColor;
            } else {
                color = validColor;
            }
        } else {
            if( selected ) {
                color = INVALID_VALUE_COLOR_SELECTED;
            } else {
                color = INVALID_VALUE_COLOR;
            }
        }
        
        label.setBackground( color );
        
    }


    /**
     * Sets a <code>JTextComponent</code>'s valid state,
     * 
     * @param textField The text component
     * @param valid The valid state
     */
    private static final void setTextComponentValid( JTextComponent textField, boolean valid ) {
        
        Color color = INVALID_VALUE_COLOR;
        if( valid ) {
            color = UIManager.getColor( "TextField.background" );
        }
        
        textField.setBackground( color );
        
    }
    
    
    /**
     *
     * 
     * @param panel
     * @param valid
     */
    private static final void setPanelValid( JPanel panel, boolean valid ) {
        
        Color color = INVALID_VALUE_COLOR;
        if( valid ) {
            color = UIManager.getColor( "Panel.background" );
        }
        
        panel.setBackground( color );
        
    }


    /**
     * Sets a <code>JSpinner</code>'s valid state,
     * 
     * @param spinner The spinner component
     * @param valid The valid state
     */
    private static final void setSpinnerValid( JSpinner spinner, boolean valid ) {
        
        JComponent editor = spinner.getEditor();
        if( editor instanceof JSpinner.DefaultEditor ) {
            setComponentValid( ((JSpinner.DefaultEditor)editor).getTextField(), valid );
        } else {
            throw new IllegalArgumentException( "Unknown spinner editor type: " + editor.getClass() );
        }
        
    }


    /**
     * Sets a <code>JComboBox</code>'s valid state,
     * 
     * @param comboBox The combo box component
     * @param valid The valid state
     */
    private static final void setComboBoxValid( JComboBox comboBox, boolean valid ) {
        
        Border border = null;
        if( valid ) {
            border = BorderFactory.createEmptyBorder( 1, 1, 1, 1 );
        } else {
            border = BorderFactory.createLineBorder( INVALID_VALUE_COLOR, 1 );
        }
        
        comboBox.setBorder( border );
        
    }



    /**
     * Get the combined bounds of the given screen indexes.
     * <p>
     * This will calculate the largest rectangle that will fit into all
     * the given screen indexes and will also account for any insets (e.g.
     * task bar, start menu, etc).
     * 
     * @param forScreenIndexes The screen indexes to combine
     * @param defaultBounds If for some reason the environment isn't returning any devices,
     * this size will be used.
     * @return The combined bounds
     */
    public static Rectangle getCombinedScreenBounds( List<Integer> forScreenIndexes, Rectangle defaultBounds ) {
        
        int x1 = Integer.MAX_VALUE;
        int x2 = 0;
        int y1 = 0;
        int y2 = Integer.MAX_VALUE;
        
        // This assumes monitors in a horizontal layout.
        // will not work for vertical layout or combination of both (is that possible?)
        for( Integer screenIndex : forScreenIndexes ) {
            
            Rectangle screenBounds = getScreenBounds( screenIndex.intValue(), defaultBounds );
            if( x1 > screenBounds.x ) {
                x1 = screenBounds.x;
            }
            if( x2 < screenBounds.x + screenBounds.width ) {
                x2 = screenBounds.x + screenBounds.width;
            }
            if( y1 < screenBounds.y ) {
                y1 = screenBounds.y;
            }
            if( y2 > screenBounds.y + screenBounds.height ) {
                y2 = screenBounds.y + screenBounds.height;                
            }
        }
        
        return new Rectangle( x1, y1, x2-x1, y2-y1 );
        
    }
    
    
    /**
     * Get the bounds of the given screen index.
     * <p>
     * The bounds will account for any insets defined for that screen
     * (e.g. taskbar, start menu, etc).
     * 
     * @param forScreenIndex The screen index
     * @param defaultBounds If for some reason the environment isn't returning any devices,
     * this size will be used.
     * @return The screen's bounds
     */
    public static Rectangle getScreenBounds( int forScreenIndex, Rectangle defaultBounds ) {
        
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] graphicsDevices = graphicsEnvironment.getScreenDevices();
        if( graphicsDevices.length == 0 ) {
            // Shouldn't happen - must be at least one screen
            // TODO: log error?  
            return defaultBounds;
        }
        
        if( forScreenIndex >= graphicsDevices.length ) {
            forScreenIndex = 0;
        }
        
        GraphicsConfiguration config = graphicsDevices[forScreenIndex].getDefaultConfiguration();
        Rectangle screenBounds = config.getBounds();
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets( config );
        Rectangle usableBounds = new Rectangle();
        usableBounds.x = screenBounds.x + insets.left + 1;
        usableBounds.y = screenBounds.y + insets.top + 1;
        usableBounds.width = screenBounds.width - ( insets.left + insets.right + 1 );
        usableBounds.height = screenBounds.height - ( insets.top + insets.bottom + 1 );
        return usableBounds;
    }
    
    
    /**
     * Get the indexes of all screens that the given bounds intersect.
     * 
     * @param forBounds The bounds to check (e.g. a Window's saved bounds).
     * @return A list of screen indexes that contain at least a portion of the
     * given bounds.
     */
    public static List<Integer> getScreens( Rectangle forBounds ) {
        
        List<Integer> screenIndexes = new ArrayList<Integer>();
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] graphicsDevices = graphicsEnvironment.getScreenDevices();
        for( int screenIndex = 0 ; screenIndex <graphicsDevices.length ; screenIndex++ ) {
            GraphicsConfiguration defaultConfiguration 
                    = graphicsDevices[screenIndex].getDefaultConfiguration();
            Rectangle screenBounds = defaultConfiguration.getBounds();
            if( forBounds.intersects( screenBounds ) ) { 
                screenIndexes.add( Integer.valueOf( screenIndex ) );
            }
        }
        
        return screenIndexes;
    }
    
    
    public static Point convertScreenLocationToParentLocation( Window forChildWindow, Point screenLocation ) {
        
        Window parentWindow = forChildWindow.getOwner();
        
        if( parentWindow == null ) {
            return screenLocation;
        }
        Point parentLocationOnScreen = parentWindow.getLocationOnScreen();
        
        return new Point( 
                screenLocation.x - parentLocationOnScreen.x,
                screenLocation.y - parentLocationOnScreen.y );
        
    }
    
    
    public static Point restrictChildBoundsToParentMonitors( Window window, Point proposedScreenLocation ) {
        
        Rectangle parentScreenArea = null;
        
        Rectangle childScreenArea = new Rectangle(
                proposedScreenLocation,
                window.getSize() );

        Window parentWindow = window.getOwner();
        if( parentWindow == null ) {
            parentScreenArea = childScreenArea;
        } else {
            parentScreenArea = new Rectangle( 
                    parentWindow.getLocationOnScreen(),
                    parentWindow.getSize() );
        }
        
        Rectangle availableScreenArea = UiUtil.getCombinedScreenBounds( UiUtil.getScreens( parentScreenArea ), 
                new Rectangle( 0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE ) );

        Point newScreenLocation = new Point( proposedScreenLocation );
        
        // Restrain dialog to screen
        if( newScreenLocation.x > availableScreenArea.x + availableScreenArea.width - childScreenArea.width ) {
            newScreenLocation.x = availableScreenArea.x + availableScreenArea.width - childScreenArea.width;
        }
        if( newScreenLocation.y > availableScreenArea.y + availableScreenArea.height - childScreenArea.height ) {
            newScreenLocation.y = availableScreenArea.y + availableScreenArea.height - childScreenArea.height;
        }
        if( newScreenLocation.x < availableScreenArea.x ) {
            newScreenLocation.x = availableScreenArea.x;
        }
        if( newScreenLocation.y < availableScreenArea.y ) {
            newScreenLocation.y = availableScreenArea.y;
        }
        
        return newScreenLocation;
        
    }
    
    public static Icon getIconForOptionPaneMessageType( int messageType ) {
        Icon icon = null;
        switch ( messageType ) {
            case JOptionPane.INFORMATION_MESSAGE:
                icon = UIManager.getIcon("OptionPane.informationIcon");
                break;
            case JOptionPane.WARNING_MESSAGE:
                icon = UIManager.getIcon("OptionPane.warningIcon");
                break;
            case JOptionPane.ERROR_MESSAGE:
                icon = UIManager.getIcon("OptionPane.errorIcon");
                break;
            case JOptionPane.QUESTION_MESSAGE:
                icon = UIManager.getIcon( "OptionPane." );
            default:
                icon = UIManager.getIcon("OptionPane.informationIcon");
        }
        return icon;
    }
    
    /**
     * A simple implementation of Java2D's Dimension2D interface.
     *
     * @author mnielsen
     */
    public static final class Dimension2D extends java.awt.geom.Dimension2D {

        private double width;
        
        private double height;
        
        public Dimension2D() {
            width = 0d;
            height = 0d;
        }

        public Dimension2D( double width, double height ) {
            super();
            this.width = width;
            this.height = height;
        }

        /**
         * @see java.awt.geom.Dimension2D#getWidth()
         */
        @Override
        public double getWidth() {
            return width;
        }

        /**
         * @see java.awt.geom.Dimension2D#getHeight()
         */
        @Override
        public double getHeight() {
            return height;
        }

        /**
         * 
         * @see java.awt.geom.Dimension2D#setSize(double, double)
         */
        @Override
        public void setSize( double width, double height ) {
            this.width = width;
            this.height = height;
        }
        
    }
    
    
    /**
     * Recursively expand all nodes in the tree starting at
     * the specified parent tree path.
     * 
     * @param tree the tree to expand.
     * @param parent the parent tree path.
     */
    public static void expandAll( JTree tree, TreePath parent ) {
        TreeNode node = (TreeNode)parent.getLastPathComponent();

        if ( node.getChildCount() >= 0 ) {
            for ( Enumeration<?> e = node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode)e.nextElement();
                TreePath path = parent.pathByAddingChild( n );
                expandAll( tree, path );
            }
        }
        tree.expandPath( parent );
    }

    
    /**
     * Add a key listener to a spinner.  Assumes spinner is using the JSpinner.DefaultEditor.
     * Code is still safe if not, however, no listener will be added.
     * 
     * @param spinner The spinner
     * @param keyListener The listener
     */
    public static final void addSpinnerKeyListener( JSpinner spinner, KeyListener keyListener ) {
        
        JComponent editor = spinner.getEditor();
        if( editor instanceof JSpinner.DefaultEditor ) {
            ((JSpinner.DefaultEditor)editor).getTextField().addKeyListener( keyListener );
        }
        
    }

    
    /**
     * Sets the number of columns in a spinner's text field.
     * Assumes spinner is using the JSpinner.DefaultEditor.
     * Code is still safe if not, however, no listener will be added.
     * 
     * @param spinner The spinner
     * @param columns The number of columns
     */
    public static final void setSpinnerColumns( JSpinner spinner, int columns ) {
        
        JComponent editor = spinner.getEditor();
        if( editor instanceof JSpinner.DefaultEditor ) {
            ((JSpinner.DefaultEditor)editor).getTextField().setColumns( columns );
        }
        
    }


    /**
     * Reduce the horizontal margin on the given button while leaving the vertical
     * margin the same.  The margin is reduced to a standard default width.
     * <p>
     * May be used where horizontal real estate is in a button bar is a concern
     * (e.g. with paged table). 
     * 
     * @param button The button
     */
    public static final void reduceHorizontalMargins( AbstractButton button ) {
        setHorizontalMargins( button, DEFAULT_REDUCED_HORIZONTAL_BUTTON_MARGIN );
    }
    

    /**
     * Set the horizontal margin on the given button while leaving the vertical
     * margin the same.
     * <p>
     * May be used where horizontal real estate is in a button bar is a concern
     * (e.g. with paged table). 
     * 
     * @param button The button
     * @param marginSize The margin size to set
     */
    public static final void setHorizontalMargins( AbstractButton button, int marginSize ) {
        Insets defaultMargin = button.getMargin();
        button.setMargin( new Insets( defaultMargin.top, marginSize, defaultMargin.bottom, marginSize ) );
    }
    
    
    public static final void standardizePopupMenu( JComponent menu ) {
        
        JPopupMenu popupMenu = null;
        
        if( menu instanceof JPopupMenu ) {
            popupMenu = (JPopupMenu)menu;
        } else if( menu instanceof JMenu ) {
            popupMenu = ((JMenu)menu).getPopupMenu();
        }
        
        if( popupMenu == null ) {
            return;
        }
        
        int count = popupMenu.getComponentCount();
        for( int i=0; i<count; i++ ) {
            
            Component menuItem = popupMenu.getComponent( i );
        
            if( menuItem instanceof JLabel ) {
                truncateLabelIfLong( (JLabel)menuItem, MAXIMUM_MENU_CHARACTER_WIDTH );
            } else if( menuItem instanceof JMenuItem ) {
                truncateMenuItemIfLong( (JMenuItem)menuItem, MAXIMUM_MENU_CHARACTER_WIDTH );
            }
            
        }
        
    }

    
    public static final Color lightenColor( Color color, float factor ) {

        if ( factor < 0.0f || factor > 1.0f ) {
            throw new IllegalArgumentException( "Factor must be between 0.0 and 1.0." );
        }            
        
        float hsbVals[] = Color.RGBtoHSB( color.getRed(), color.getGreen(), color.getBlue(), null );
        float brightness = hsbVals[2] + ( hsbVals[2] * factor );
        if ( brightness >= 1.0f ) {
            return Color.WHITE;
        }
        else {
            return Color.getHSBColor( hsbVals[0], hsbVals[1], brightness );
        }
    }

    
    public static final Color darkenColor( Color color, float factor ) {
        
        if ( factor < 0.0f || factor > 1.0f ) {
            throw new IllegalArgumentException( "Factor must be between 0.0 and 1.0." );
        }            
        
        float hsbVals[] = Color.RGBtoHSB( color.getRed(), color.getGreen(), color.getBlue(), null );
        float brightness = hsbVals[2] - ( hsbVals[2] * factor );
        if ( brightness < 0.0f ) {
            return Color.BLACK;
        }
        else {
            return Color.getHSBColor( hsbVals[0], hsbVals[1], brightness );
        }
    }
}
