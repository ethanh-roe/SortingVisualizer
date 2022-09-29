package sortingvisualizer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.Timer;

/**
 *
 * @author eroe
 */
public class mainJpanel extends javax.swing.JPanel implements ActionListener {

    ArrayList<Line> Mainlist = new ArrayList<>();
    ArrayList<Line> templist = new ArrayList<>();
    Timer timer;
    static final int PanelWidth = 1024;                                         // window width
    static final int PanelHeight = 800;                                         // window height
    int speed;                                                                  // speed sets the delay between gui updates in ms
    static int comparisons = 0;                                                 // keeps track of the # of comparisons    
    static int writesToArray = 0;                                               // keeps track of the # of writes to the main arraylist
    static int numElements = 100;                                               // holds # of elements
    static int stroke = (PanelWidth / numElements) - 1;                             // holds stroke size 

    public mainJpanel() {
        initComponents();
        addLines(numElements);                                                  // adding lines to render

        timer = new Timer(speed, this);                                         // setting up and
        timer.start();                                                          // starting gui update timer
        setBackground(Color.darkGray);                                          // sets up window colors
    }

    public void actionPerformed(ActionEvent e) {                                // this is the action triggered by the timer
        ComparisonLabel.setText(comparisons + " Comparisons");                  // updates comparisons label
        WritestoArrayLabel.setText(writesToArray + " Writes to Main Array");    // updates writes to array label

        speed = SpeedSlider.getValue();                                         // check the value defined by the slider to set the speed

        SpeedSliderLabel.setText("updates every " + SpeedSlider.getValue() + "MS");//updates the slider speed label

        int lastNumElements = numElements;
        numElements = ElementsSlider.getValue();
        stroke = (PanelWidth / numElements) - 1;
        if (lastNumElements != ElementsSlider.getValue()) {
            addLines(numElements);                                              // adding lines to render
        }

        ElementsLabel.setText(ElementsSlider.getValue() + " Elements");
        repaint();                                                              // repaints all the UI elements including lines
    }

    public void paintComponent(Graphics g) {                                    // paints the UI everytime repaint() is called
        super.paintComponent(g);
        drawList(g);                                                            // calls the draw list method
    }

    public void drawList(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(stroke));                                  // sets the line thickness depending on the number of elements
        for (Line l : Mainlist) {                                               // goes through every element in the list
            g2.setColor(l.color);                                               // sets each lines stroke color the color assigned in its attribute
            g2.drawLine(l.x1, l.y1, l.x2, l.y2);                                // draws each line with the appropriate x and y values
        }

    }

    public void addLines(int s) {                                               // called in constructor to initialize the main list with line objects
        Mainlist.clear();                                                       // clears the list so we dont increase the list by 100 every press
        int size = s;                                                           // size of the list ie how many elements to fill the list with 

        int x1 = PanelWidth - (numElements * (stroke + 1)) + (stroke / 2);               // starts drawing the lines from left border <- right border 6px from right border
        int x2 = PanelWidth - (numElements * (stroke + 1)) + (stroke / 2);
        int y1 = 0;                                                             // the largest element is drawn 400px from the top of the windowto the bottom of the window
        int y2 = PanelHeight;                                                   // 600 is the bottom of the window

        for (int i = 0; i < size; i++) {                                        // adds 's' amount of lines
            y1 = (int) (Math.random() * (PanelHeight - 100)) + 100;
            Mainlist.add(new Line(x1, y1, x2, y2, Color.white));                // gives all lines an x and y coordinates and a starting color value of white
            x1 += stroke + 1;                                                     // shifts the line that follows
            x2 += stroke + 1;                                                     // 'x'px to the left <-
        }

    }

    private void ResetLists() {                                                 // empties the temp list to the main list after scramble
        for (int i = 0; i < templist.size(); i++) {
            Mainlist.add(templist.get(i));

        }
        templist.clear();

    }

    public void scramble() {                                                    // scrambles array randomly
        int x = PanelWidth - (numElements * (stroke + 1)) + (stroke / 2);
        int rand = 0;
        int size = Mainlist.size();
        for (int j = 0; j < size; j++) {
            rand = (int) (Math.random() * Mainlist.size());                     // picks a random number 0 <-> size of list
            Mainlist.get(rand).setxs(x, x);                                     // picks random index and gives it x coord
            Mainlist.get(rand).setColor(Color.white);
            templist.add(Mainlist.get(rand));                                   // adds the index to the templist
            Mainlist.remove(rand);                                              // removes the index from mainlist so there are no repeats
            x += stroke + 1;
        }
        ResetLists();

    }

    public void MergeSort(ArrayList<Line> list, int lef, int rig) {             //main merge sort method

        if (lef < rig) {

            int m = lef + (rig - lef) / 2;                                       // determines the mid point by taking the left val + (right val - left val)
            // and then divides it by 2
            MergeSort(list, lef, m);                                            // recursively calls the same process until the left half is split into lists
            // with a size of 1
            MergeSort(list, m + 1, rig);                                        // recursively calls the same process until the right half is also split up

            // Merge the sorted halves
            Merge(list, lef, m, rig);                                           // calls the method to merge all the split up liss into one but sorted

        }

    }

    private void Merge(ArrayList<Line> arr, int lef, int mid, int rig) {        // helper method to merge sort that merges the split arrays

        int n1 = mid - lef + 1;                                                 // determines size of the left array
        int n2 = rig - mid;                                                     // determines size of the right array

        ArrayList<Line> left = new ArrayList<>();                               // creates an array list for the left half
        ArrayList<Line> right = new ArrayList<>();                              // creates an array list for the right half

        for (int i = 0; i < n1; ++i) {                                           // adds elements to the left list
            left.add(arr.get(lef + i));
        }
        for (int j = 0; j < n2; ++j) {                                           //adds elements to the right list
            right.add(arr.get(mid + 1 + j));
        }
        int lefti = 0;                                                      // tracks left index  
        int righti = 0;                                                     // tracks right index
        int k = lef;                                                        // tracks index of main array
        int x = Mainlist.get(k).getx1();

        while (lefti < n1 && righti < n2) {                                     // while both indices are less than their size
            comparisons++;
            left.get(lefti).setColor(Color.red);
            right.get(righti).setColor(Color.red);
            if (left.get(lefti).getYSize() <= right.get(righti).getYSize()) {   //if the left is smaller than the right... sort the left one in first
                Mainlist.set(k, left.get(lefti));
                Mainlist.get(k).setxs(x, x);

                writesToArray++;
                lefti++;
                x += stroke + 1;
                pauseTimer(speed);
                left.get(lefti - 1).setColor(Color.white);
                Mainlist.get(k).setColor(Color.white);
            } else {                                                              // if the right is smaller than sort the right one in
                Mainlist.set(k, right.get(righti));
                Mainlist.get(k).setxs(x, x);
                Mainlist.get(k).setColor(Color.red);
                writesToArray++;
                righti++;
                x += stroke + 1;
                pauseTimer(speed);
                right.get(righti - 1).setColor(Color.red);
                Mainlist.get(k).setColor(Color.white);
            }
            pauseTimer(speed);
            k++;                                                                // moves to next index in main array

        }
        while (lefti < n1) {                                                    // accounts for any left over elements in the left array
            left.get(lefti).setColor(Color.red);
            Mainlist.set(k, left.get(lefti));
            Mainlist.get(k).setxs(x, x);
            Mainlist.get(k).setColor(Color.red);
            writesToArray++;
            lefti++;
            x += stroke + 1;
            k++;
            pauseTimer(speed);
            left.get(lefti - 1).setColor(Color.white);
            Mainlist.get(k - 1).setColor(Color.white);
        }

        while (righti < n2) {                                                   // accounts for any left over elements in the right array
            Mainlist.set(k, right.get(righti));
            Mainlist.get(k).setxs(x, x);
            Mainlist.get(k).setColor(Color.red);
            right.get(righti).setColor(Color.red);
            writesToArray++;
            righti++;
            k++;
            x += stroke + 1;
            pauseTimer(speed);
            right.get(righti - 1).setColor(Color.white);
            Mainlist.get(k - 1).setColor(Color.white);
        }

    }

    public void BogoSort() {                                                    // main bogo sort method -- randomly scrambles array until it is sorted
        int tempx1 = 0;
        int tempx2 = 0;
        while (isSorted(Mainlist) == false) {                                   // while the list isnt sorted
            for (int i = 0; i < Mainlist.size(); i++) {
                int rand = (int) (Math.random() * i);                           // scramble the list
                tempx1 = Mainlist.get(i).getx1();
                tempx2 = Mainlist.get(i).getx2();
                Mainlist.get(i).setxs(Mainlist.get(rand).getx1(), Mainlist.get(rand).getx2());
                Mainlist.get(rand).setxs(tempx1, tempx2);
                writesToArray++;
            }
            pauseTimer(speed);
        }
    }

    private boolean isSorted(ArrayList<Line> list) {                            // helper method to bogo sort to check if the array is sorted
        for (int i = 1; i < list.size(); i++) {
            comparisons++;
            if (list.get(i).getYSize() < list.get(i - 1).getYSize()) {
                return false;

            }

        }
        return true;
    }

    public void SelectionSort() {                                               // main selecton sort method -- finds smallest element in unsorted section
        // and sorts it to the bottom
        int tempx1 = 0;
        int tempx2 = 0;

        for (int j = 0; j < Mainlist.size(); j++) {                             //loop to move the index of the sorted part of the array
            int currentMin = j;
            pauseTimer(speed);
            for (int i = j + 1; i < Mainlist.size(); i++) {                     //loops through each element in the unsorted part of the array
                Mainlist.get(i).setColor(Color.red);
                Mainlist.get(currentMin).setColor(Color.blue);
                comparisons++;
                if (Mainlist.get(i).getYSize() < Mainlist.get(currentMin).getYSize()) { // checks this element to the current smallest element
                    Mainlist.get(currentMin).setColor(Color.white);
                    currentMin = i;
                    Mainlist.get(currentMin).setColor(Color.blue);
                }
                pauseTimer(speed);
                Mainlist.get(i).setColor(Color.white);

            }
            pauseTimer(speed);
            tempx1 = Mainlist.get(j).getx1();
            tempx2 = Mainlist.get(j).getx2();

            Mainlist.get(j).setxs(Mainlist.get(currentMin).getx1(), Mainlist.get(currentMin).getx2());
            Mainlist.get(currentMin).setxs(tempx1, tempx2);

            Collections.swap(Mainlist, j, currentMin);
            writesToArray++;
            Mainlist.get(j).setColor(Color.green);
        }

    }

    public void BubbleSort() {                                                  // main bubble sort method -- compares elements that are neighbors and swaps
        // them if they are out of order
        int swaps = 1;
        int tempx1 = 0;
        int tempx2 = 0;

        while (swaps > 0) {                                                     // while there are still elements being swapped
            swaps = 0;
            for (int i = 1; i < Mainlist.size(); i++) {
                comparisons++;
                Mainlist.get(i).setColor(Color.red);
                Mainlist.get(i - 1).setColor(Color.red);
                if (Mainlist.get(i - 1).getYSize() > Mainlist.get(i).getYSize()) { // //if the object in index n-1 is taller than the object in index n... SWAP THEM
                    tempx1 = Mainlist.get(i - 1).getx1();
                    tempx2 = Mainlist.get(i - 1).getx2();

                    Mainlist.get(i - 1).setxs(Mainlist.get(i).getx1(), Mainlist.get(i).getx2());

                    Mainlist.get(i).setxs(tempx1, tempx2);

                    Collections.swap(Mainlist, i, i - 1);
                    writesToArray++;

                    swaps++;                                                    //increase swap variable because we swapped an element
                }
                pauseTimer(speed);
                Mainlist.get(i).setColor(Color.white);
                Mainlist.get(i - 1).setColor(Color.white);

            }

        }

    }

    public void SortComplete() {                                                // called when sort is completed cool final effect

        for (Line L : Mainlist) {
            L.setColor(Color.white);

        }
        for (Line l : Mainlist) {
            l.setColor(Color.green);
            pauseTimer(10);

        }
        SortButton.setEnabled(true);                                            // re - enables sort button because we are done sorting
    }

    public void pauseTimer(int n) {
        try {
            Thread.sleep(n);                                                        // method that pauses the sort
        } catch (InterruptedException ex) {

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        ScrambleButton = new javax.swing.JButton();
        AlgoTypeComboBox = new javax.swing.JComboBox<>();
        SortButton = new javax.swing.JButton();
        SpeedSlider = new javax.swing.JSlider();
        SpeedSliderLabel = new javax.swing.JLabel();
        ComparisonLabel = new javax.swing.JLabel();
        WritestoArrayLabel = new javax.swing.JLabel();
        ElementsSlider = new javax.swing.JSlider();
        ElementsLabel = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(1024, 800));
        setSize(new java.awt.Dimension(1024, 800));

        ScrambleButton.setText("Scramble");
        ScrambleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ScrambleButtonActionPerformed(evt);
            }
        });

        AlgoTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Choose Sort Type", "Merge Sort", "Selection Sort", "Bubble Sort", "Bogo Sort" }));
        AlgoTypeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AlgoTypeComboBoxActionPerformed(evt);
            }
        });

        SortButton.setText("Sort");
        SortButton.setPreferredSize(new java.awt.Dimension(85, 23));
        SortButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SortButtonActionPerformed(evt);
            }
        });

        SpeedSlider.setMajorTickSpacing(100);
        SpeedSlider.setMaximum(1001);
        SpeedSlider.setMinimum(1);
        SpeedSlider.setMinorTickSpacing(10);
        SpeedSlider.setSnapToTicks(true);
        SpeedSlider.setValue(501);

        SpeedSliderLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        SpeedSliderLabel.setText("500MS");

        ComparisonLabel.setText("comparisons ->");

        WritestoArrayLabel.setText("writes to array ->");

        ElementsSlider.setMajorTickSpacing(100);
        ElementsSlider.setMaximum(500);
        ElementsSlider.setMinimum(10);
        ElementsSlider.setMinorTickSpacing(10);
        ElementsSlider.setSnapToTicks(true);
        ElementsSlider.setValue(100);

        ElementsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ElementsLabel.setText("100 Elements");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(WritestoArrayLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                    .addComponent(ComparisonLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ScrambleButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SortButton, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ElementsSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ElementsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(SpeedSliderLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(SpeedSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(52, 52, 52)
                .addComponent(AlgoTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(221, 221, 221))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(AlgoTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(SpeedSliderLabel)
                                    .addComponent(ElementsLabel))
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(SpeedSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ElementsSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(ScrambleButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(SortButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(ComparisonLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(7, 7, 7)
                .addComponent(WritestoArrayLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(708, Short.MAX_VALUE))
        );
    }// </editor-fold>                        

    private void ScrambleButtonActionPerformed(java.awt.event.ActionEvent evt) {                                               
        new Thread(new Runnable() {                                             // method that creates a new thread and then scrambles the array when the
            public void run() {                                                 // button is pressed
                scramble();
            }
        }).start();
    }                                              

    private void AlgoTypeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        //index 0 -> choose sort type
        //index 1 -> merge sort
        //index 2 -> selection sort
        //index 3 -> bubble sort
        //index 4 -> bogo sort
    }                                                

    private void SortButtonActionPerformed(java.awt.event.ActionEvent evt) {                                           
        SortButton.setEnabled(false);                                           // method that starts the selected sort
        ScrambleButton.setEnabled(false);
        AlgoTypeComboBox.setEnabled(false);
        ElementsSlider.setEnabled(false);
        comparisons = 0;                                                        // sets # of compares to 0
        writesToArray = 0;                                                      // sets # of writes to array to 0
        new Thread(new Runnable() {                                             // creates a new thread for sorts to run on
            public void run() {

                int type = AlgoTypeComboBox.getSelectedIndex();                 // assigns index of option selected

                switch (type) {                                                 // switch statement to call the sort method corresponding to the selected sort
                    case 0:                                                     // ind 0 = no sort selected so we do nothing
                        SortButton.setEnabled(true);
                        break;
                    case 1:                                                     // ind 1 = merge sort so we call the merge sort method
                        MergeSort(Mainlist, 0, Mainlist.size() - 1);
                        SortComplete();
                        break;
                    case 2:                                                     // ind 2 = slection sort so we call the selection sort method
                        SelectionSort();
                        SortComplete();
                        break;
                    case 3:                                                     // ind 3 =  bubble sort so we call the bubble sort method
                        BubbleSort();
                        SortComplete();
                        break;
                    case 4:                                                     // ind 4 = bogo sort so we call the bogo sort method
                        BogoSort();

                        SortComplete();
                        break;
                }
                ElementsSlider.setEnabled(true);                                // re enables the speed slider because we are done sorting
                ScrambleButton.setEnabled(true);                                // re enables the scramble button because we are done sorting
                AlgoTypeComboBox.setEnabled(true);                              // re enables the combo box because we are done sorting
                SortButton.setEnabled(true);                                    // re enables the sort button because we are done sorting
            }

        }).start();
    }                                          


    // Variables declaration - do not modify                     
    private javax.swing.JComboBox<String> AlgoTypeComboBox;
    private javax.swing.JLabel ComparisonLabel;
    private javax.swing.JLabel ElementsLabel;
    private javax.swing.JSlider ElementsSlider;
    private javax.swing.JButton ScrambleButton;
    private javax.swing.JButton SortButton;
    private javax.swing.JSlider SpeedSlider;
    private javax.swing.JLabel SpeedSliderLabel;
    private javax.swing.JLabel WritestoArrayLabel;
    // End of variables declaration                   

}
