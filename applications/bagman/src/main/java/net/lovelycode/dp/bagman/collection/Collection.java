package net.lovelycode.dp.bagman.collection;

import java.io.File;

public class Collection {
	
	public enum CollectionType {
		/** A plain folder with files - all hashes and metadata are held in the Collection Register. */
		PLAIN_FOLDER,
		/* A folder, with checksums for each file.*/
		CHECKSUMS_PER_FILE,
		/* A folder, with a single checksum file per folder.*/
		CHECKSUMS_PER_FOLDER,
		/* A folder with a manifest at the top-level.*/
		CHECKSUM_MANIFEST,
		/* A BagIt bag.*/
		BAGIT
	}
	
	private CollectionType type;
	
	private File rootFolder;
	
	private String ChecksumType = "MD5";
	
	private String DefaultFilenameForFolderChecksums = "checksums";
	
	private String DefaultFilenameForManifest = "manifest";
	
}
