package io.biin.biin.dal.service;

import android.app.Application;

import com.octo.android.robospice.SpiceService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.binary.InFileBitmapObjectPersister;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.string.InFileStringObjectPersister;

/**
 * Created by Ivan on 1/27/16.
 */
public class testservice extends SpiceService {
    @Override
    public CacheManager createCacheManager(Application application) throws CacheCreationException {
        CacheManager cacheManager = new CacheManager();

        // init
        InFileStringObjectPersister inFileStringObjectPersister = new InFileStringObjectPersister(application);
        InFileBitmapObjectPersister inFileBitmapObjectPersister = new InFileBitmapObjectPersister(application);

        cacheManager.addPersister(inFileStringObjectPersister);
        cacheManager.addPersister(inFileBitmapObjectPersister);
        return cacheManager;
    }
}
