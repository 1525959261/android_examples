/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.nostra13.example.universalimageloader;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nostra13.example.universalimageloader.Constants.Extra;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class ImagePagerActivity extends BaseActivity {

	private static final String STATE_POSITION = "STATE_POSITION";

	DisplayImageOptions options;
    int idx;

    MutipleTouchViewPager pager;
    private PhotoViewAttacher mAttacher;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_image_pager);

		Bundle bundle = getIntent().getBundleExtra("data");
		assert bundle != null;
		String[] imageUrls = bundle.getStringArray(Extra.IMAGES); // ��Ҫ�鿴��ͼƬ
        idx = bundle.getInt("idx"); // ѡ���ͼƬ����λ��

//		options = new DisplayImageOptions.Builder()
//			.showImageForEmptyUri(R.drawable.ic_empty)
//			.showImageOnFail(R.drawable.ic_error)
//			.resetViewBeforeLoading(true)
//			.cacheOnDisc(true)
//			.imageScaleType(ImageScaleType.EXACTLY)
//			.bitmapConfig(Bitmap.Config.RGB_565)
//			.considerExifParams(true)
//			.displayer(new FadeInBitmapDisplayer(300))
//			.build();

        options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.ic_launcher) //����ͼƬ�������ڼ���ʾ��ͼƬ
                .showImageForEmptyUri(R.drawable.ic_empty)//����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
                .showImageOnFail(R.drawable.ic_error)  //����ͼƬ����/��������д���ʱ����ʾ��ͼƬ
                .cacheInMemory(true)//�������ص�ͼƬ�Ƿ񻺴����ڴ���
                .cacheOnDisc(true)//�������ص�ͼƬ�Ƿ񻺴���SD����
                .considerExifParams(true)  //�Ƿ���JPEGͼ��EXIF��������ת����ת��
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//����ͼƬ����εı��뷽ʽ��ʾ
                .bitmapConfig(Bitmap.Config.ARGB_8888)//����ͼƬ�Ľ�������
                //.delayBeforeLoading(int delayInMillis)//int delayInMillisΪ�����õ�����ǰ���ӳ�ʱ��
                //����ͼƬ���뻺��ǰ����bitmap��������
                //.preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)//����ͼƬ������ǰ�Ƿ����ã���λ
                .displayer(new RoundedBitmapDisplayer(20))//�Ƿ�����ΪԲ�ǣ�����Ϊ����
                .displayer(new FadeInBitmapDisplayer(300))//�Ƿ�ͼƬ���غú���Ķ���ʱ��
                .build();//�������

		pager = (MutipleTouchViewPager) findViewById(R.id.pager);

        /**
         * �������,��ôviewPager�Ὣҳ�滺������,����Ҫע��,�����ù���ʱ,���ܻ�����ڴ����
         */
        pager.setOffscreenPageLimit(2);

		pager.setAdapter(new ImagePagerAdapter(imageUrls));
		pager.setCurrentItem(idx);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, pager.getCurrentItem());
	}

	private class ImagePagerAdapter extends PagerAdapter {

		private String[] images;
		private LayoutInflater inflater;

		ImagePagerAdapter(String[] images) {
			this.images = images;
			inflater = getLayoutInflater();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return images.length;
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			View imageLayout = inflater.inflate(R.layout.item_pager_image, view, false);
			assert imageLayout != null;
			final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);

			final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);

			imageLoader.displayImage("file://" + images[position], imageView, options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					spinner.setVisibility(View.VISIBLE);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					String message = null;
					switch (failReason.getType()) {
						case IO_ERROR:
							message = "Input/Output error";
							break;
						case DECODING_ERROR:
							message = "Image can't be decoded";
							break;
						case NETWORK_DENIED:
							message = "Downloads are denied";
							break;
						case OUT_OF_MEMORY:
							message = "Out Of Memory error";
							break;
						case UNKNOWN:
							message = "Unknown error";
							break;
					}
					Toast.makeText(ImagePagerActivity.this, message, Toast.LENGTH_SHORT).show();

					spinner.setVisibility(View.GONE);
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    spinner.setVisibility(View.GONE);
                    mAttacher = new PhotoViewAttacher(imageView); // ֧�ַŴ���С
                    mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                        @Override
                        public void onPhotoTap(View arg0, float arg1, float arg2) {
                            finish();
                        }
                    });

                    mAttacher.update();
                }
			});

			view.addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	}
}