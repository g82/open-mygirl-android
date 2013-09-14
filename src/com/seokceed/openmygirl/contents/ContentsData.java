package com.seokceed.openmygirl.contents;

import com.seokceed.openmygirl.R;

public enum ContentsData {
	
	/*
	 * add contents here
	 * CONTETNS(meta data array, thumbnail, tag)
	 */
	
	BENE(R.array.meta_bene, R.drawable.thumb_bene, "BENE"),
	HUMAN(R.array.meta_human, R.drawable.thumb_human, "HUMAN"),
	LOL(R.array.meta_lol),
	POKE(R.array.meta_poke),
	SINGROOM(R.array.meta_singroom, R.drawable.thumb_sing,"SINGROOM"),
	KKUKKUPEDIA(R.array.meta_pedia),
	JISIK(R.array.meta_jisik, R.drawable.thumb_ebs, "EBS_JISIK"),
//	WOOPAROO(R.array.meta_wooparoo, R.drawable.thumb_wooparoo,"WOOPAROO"),
	GEOGRAPHIC(R.array.meta_jisik, R.drawable.thumb_ebs, "GEOGRAPHIC");
//	OLLEH(R.array.meta_olleh, R.drawable.thumb_olleh, "OLLEH");
	
	public static ContentsData[] arrContents = 
//		{BENE, HUMAN, JISIK, SINGROOM, WOOPAROO, OLLEH};
		{BENE, HUMAN, JISIK, SINGROOM};
	
	public ContentsData getType(int ordinal) {
		switch (ordinal) {
		case 0:
			return BENE;
		case 1:
			return HUMAN;
		case 2:
			return JISIK;
		case 3:
			return SINGROOM;
//		case 4:
//			return WOOPAROO;
//		case 5:
//			return OLLEH;
		default:
			return null;
		}
	}
	
	private int meta_id;
	private int thumb_id;
	private String content_tag;
		
	private ContentsData(int meta_dataID) {
		meta_id = meta_dataID;
	}
	
	private ContentsData(int meta_dataID, int thumbId, String tag) {
		meta_id = meta_dataID;
		thumb_id = thumbId;
		content_tag = tag;
	}

	public int getMeta_id() {
		return meta_id;
	}
	
	public int getThumb_id() {
		return thumb_id;
	}
	
	public String getContent_tag() {
		return content_tag;
	}
}
