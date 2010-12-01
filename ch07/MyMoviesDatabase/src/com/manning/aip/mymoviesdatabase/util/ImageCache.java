package com.manning.aip.mymoviesdatabase.util;

import android.graphics.Bitmap;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Naive cache that relies on HashMap's removeEldestEntry.
 * 
 * @author ccollins
 *
 */
@SuppressWarnings("serial")
public class ImageCache {

   private static final int IMAGE_CACHE_SIZE = 250;

   private final Map<String, Bitmap> cache;

   // HashMap decorator that only grows to X size
   // (note, using simple WeakHashMap is NOT a good cache for this, it uses weak references for *keys*)
   public ImageCache() {
      this.cache = Collections.synchronizedMap(new LinkedHashMap<String, Bitmap>(IMAGE_CACHE_SIZE + 1, .75F, true) {
         public boolean removeEldestEntry(Map.Entry<String, Bitmap> eldest) {
            return size() > IMAGE_CACHE_SIZE;
         }
      });
   }

   public Bitmap get(String urlString) {
      return this.cache.get(urlString);
   }

   public void put(String urlString, Bitmap bitmap) {
      this.cache.put(urlString, bitmap);
   }
}
