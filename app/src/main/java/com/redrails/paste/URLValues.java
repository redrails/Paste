package com.redrails.paste;

import java.util.UUID;

/**
 * Created by toby on 29/05/16.
 * This file has all the URL prefixes and the static variables that will be used throughout.
 * It's better to define here for more cleanliness of the code.
 */

public class URLValues {

    // To avoid caching of this number, we have to use this shitty way of handling the get
    // contents. It works.
    public final static String LASTPASTEURL = "http://ihtasham.com/paste/last_paste.txt?id="+ UUID.randomUUID();

    public final static String POSTURL = "http://ihtasham.com/paste/index.php";

    public final static String URLPREFIX = "http://ihtasham.com/paste/pastes/";

}
