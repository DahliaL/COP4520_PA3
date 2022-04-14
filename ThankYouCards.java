/*
*
* The Minotaur’s birthday party was a success. The Minotaur received a lot of presents
*  from his guests. The next day he decided to sort all of his presents and start writing “Thank you” cards.
* Every present had a tag with a unique number that was associated with the guest who gave it. Initially all
*  of the presents were thrown into a large bag with no particular order. The Minotaur wanted to take the
* presents from this unordered bag and create a chain of presents hooked to each other with special links
* (similar to storing elements in a linked-list). In this chain (linked-list) all of the presents had to be
* ordered according to their tag numbers in increasing order. The Minotaur asked 4 of his servants to help him
* with creating the chain of presents and writing the cards to his guests. Each servant would do one of three
* actions in no particular order:

1. Take a present from the unordered bag and add it to the chain in the correct location by hooking it to the
* predecessor’s link. The servant also had to make sure that the newly added present is also linked with the
*  next present in the chain.
2. Write a “Thank you” card to a guest and remove the present from the chain. To do so, a servant had to unlink
* the gift from its predecessor and make sure to connect the predecessor’s link with the next gift in the chain.
*
3. Per the Minotaur’s request, check whether a gift with a particular tag was present in the chain or not; without
*  adding or removing a new gift, a servant would scan through the chain and check whether a gift with a particular
*  tag is already added to the ordered chain of gifts or not.
*
As the Minotaur was impatient to get this task done quickly, he instructed his servants not to wait until all of the
* presents from the unordered bag are placed in the chain of linked and ordered presents. Instead, every servant was
* asked to alternate adding gifts to the ordered chain and writing “Thank you” cards. The servants were asked not to
*  stop or even take a break until the task of writing cards to all of the Minotaur’s guests was complete.

After spending an entire day on this task the bag of unordered presents and the chain of ordered presents were both
* finally empty!

Unfortunately, the servants realized at the end of the day that they had more presents than “Thank you” notes.
* What could have gone wrong?

Can we help the Minotaur and his servants improve their strategy for writing “Thank you” notes?

Design and implement a concurrent linked-list that can help the Minotaur’s 4 servants with this task. In your test,
simulate this concurrent “Thank you” card writing scenario by dedicating 1 thread per servant and assuming that the
Minotaur received 500,000 presents from his guests.
* */

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ThankYouCards {
    private volatile static ArrayList<Gift> unorderedBag;
    private static LockFreeList<Object> orderedTags;
    private volatile static ArrayList<Gift> unorderedBagToRemove;
    private volatile static int thankYouCount;

    public static void addElementToList(Gift toAdd) {
       orderedTags.add(toAdd);
    }

    public static void removeElement() {
        if(!unorderedBagToRemove.isEmpty() && orderedTags.contains(unorderedBagToRemove.get(0)))
            //System.out.println("Removing gift "+ unorderedBagToRemove.get(0).tag);
        if(!unorderedBagToRemove.isEmpty() && orderedTags.remove(unorderedBagToRemove.get(0))) {
            unorderedBagToRemove.remove(0);
            thankYouCount++;
        }
    }

    public static void performTasks() {
        int rand = ThreadLocalRandom.current().nextInt(0, 2);
        if(rand == 0) { // adding to the Linked List
            if(unorderedBag.size() == 0) {
                removeElement();
            }
            if(!unorderedBag.isEmpty()) {
                //System.out.println("Adding gift "+  unorderedBag.get(0).tag);
                addElementToList(unorderedBag.get(0));
                unorderedBag.remove(0);
            } }
        else { // writing thank you card
            removeElement();
            }
    }


    public static class servantForMinotaur implements Runnable {
        @Override
        public synchronized void run() {
            performTasks();
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the number of unordered gifts to sort through and write thank-yous for: ");
        int n = sc.nextInt();
        thankYouCount = 0;

        // creating bag
        unorderedBag = new ArrayList<>();
        unorderedBagToRemove = new ArrayList<>();

        for(int i = 0; i < n; i++) {
            Gift curr = new Gift(i);
            unorderedBag.add(curr);
            unorderedBagToRemove.add(curr);
        }

        Collections.shuffle(unorderedBag);
        Collections.shuffle(unorderedBagToRemove);

        orderedTags = new LockFreeList<>();
        Thread[] servants = new Thread[4];
        servantForMinotaur servantForMinotaur = new servantForMinotaur();

        System.out.println("The servants are ready to start!");
        long start = System.currentTimeMillis();
        while( thankYouCount < n || !unorderedBag.isEmpty()) {
            for(int i = 0; i < 4; i++ ) {
                try {
                    servants[i].join();}
                catch(NullPointerException | InterruptedException ignored) {
                }
                servants[i] = new Thread(servantForMinotaur);
                servants[i].start();
            }
        }

        long end = System.currentTimeMillis();
        long totalTime = end-start;

        System.out.println("The servants are finished!");
        System.out.println("Elapsed time is " + totalTime/1000 + " seconds.");

    }
}

class Gift {
    int tag;
    Gift(int num) {
        tag = num;
    }
}
