package com.example.anton.androidlibhomework.di.modules;

import android.widget.ImageView;

import com.example.anton.androidlibhomework.model.cache.IImageCache;
import com.example.anton.androidlibhomework.model.cache.RealmImageCache;
import com.example.anton.androidlibhomework.model.image.ImageLoader;
import com.example.anton.androidlibhomework.model.image.android.ImageLoaderGlide;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Singleton
@Module
public class ImageLoaderModule {
    @Provides
    public IImageCache cacheRealm() {
        return new RealmImageCache();
    }

    @Provides
    public ImageLoader<ImageView> imageLoader(IImageCache imageCache) {
        return new ImageLoaderGlide(imageCache);
    }
}
