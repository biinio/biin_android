package com.biin.biin.Entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ramirezallan on 8/4/16.
 */
public class BNGift {
    private String elementIdentifier;
    private String organizationIdentifier;

    private String name;
    private String message;

    private boolean hasExpirationDate = false;
    private Date expirationDate;
    private Date receivedDate;

    private List<String> sites;

    private int status;
    private List<BNMedia> media;

    private int primaryColor;
    private int secondaryColor;

    public BNGift() {
        sites = new ArrayList<>();
        media = new ArrayList<>();
    }

    public abstract class BNGiftStatus {
        public static final int NONE = 0;
        public static final int SENT = 1;
        public static final int REFUSED = 2;
        public static final int SHARED = 3;
        public static final int CLAIMED = 4;
        public static final int DELIVERED = 5;
    }
}
