package net.lovelycode.dp.bagman.collection;

import gov.loc.repository.bagit.Bag;
import gov.loc.repository.bagit.BagFactory;

public class CollectionOps {
	
	public static void makeBag() {
        BagFactory bf = new BagFactory();
        Bag bag = bf.createBag();
        //bag.addFileToPayload(null);
	}
}
