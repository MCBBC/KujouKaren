package com.example.administrator.essim.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.essim.R;
import com.example.administrator.essim.activities.DetailsActivity;
import com.example.administrator.essim.activities.ViewPagerActivity;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import jp.wasabeef.glide.transformations.BlurTransformation;
import me.gujun.android.taggroup.TagGroup;


/**
 * Created by Administrator on 2018/1/18 0018.
 */

public class FragmentPixivItem extends BaseFragment {

    private int index;
    private ImageView mImageView, mImageView2;
    private TextView mTextView, mTextView2, mTextView3, mTextView4, mTextView5;
    private TagGroup mTagGroup;
    private MyAsyncTask asyncTask;
    private ProgressDialog progressDialog;
    private CardView mCardView, mCardView2, mCardView3, mCardView4;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pixiv_item, container, false);
        index = (int) getArguments().getSerializable("index");
        if (index == ((ViewPagerActivity) getActivity()).getIndexNow()) {
            setUserVisibleHint(true);
        }
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        reFreshLayout(view);
        return view;
    }

    private void reFreshLayout(View view) {
        mImageView = view.findViewById(R.id.item_background_img);
        mImageView2 = view.findViewById(R.id.detail_img);
        Glide.with(getContext()).load(FragmentPixivLeft.mPixivRankItem.response.get(0)
                .works.get(index).work.image_urls.getPx_480mw())
                .bitmapTransform(new BlurTransformation(getContext(), 20, 2))
                .into(mImageView);
        Glide.with(getContext()).load(FragmentPixivLeft.mPixivRankItem.response.get(0)
                .works.get(index).work.image_urls.getPx_480mw())
                .into(mImageView2);
        mTextView = view.findViewById(R.id.detail_author);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailsActivity.class);
                mContext.startActivity(intent);
            }
        });
        mTextView2 = view.findViewById(R.id.detail_img_size);
        mTextView3 = view.findViewById(R.id.detail_create_time);
        mTextView4 = view.findViewById(R.id.viewed);
        mTextView5 = view.findViewById(R.id.liked);
        mCardView = view.findViewById(R.id.card_first);
        mCardView.setOnTouchListener(this);
        mCardView2 = view.findViewById(R.id.card_second);
        mCardView2.setOnTouchListener(this);
        mCardView3 = view.findViewById(R.id.card_left);
        mCardView3.setOnTouchListener(this);
        mCardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(Environment.getExternalStorageDirectory().getPath() + "/Download");
                if (!file.exists()) {
                    file.mkdir();
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TastyToast.makeText(mContext, "文件夹创建成功~",
                                    TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                        }
                    });
                }
                File file1 = new File(Environment.getExternalStorageDirectory().getPath() + "/Download/" +
                        FragmentPixivLeft.mPixivRankItem.response.get(0).works.get(index).work.getTitle() + ".jpeg");
                if (file1.exists()) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TastyToast.makeText(mContext, "该文件已存在~",
                                    TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show();
                        }
                    });
                } else {
                    asyncTask = new MyAsyncTask();
                    asyncTask.execute(FragmentPixivLeft.mPixivRankItem.response.get(0).works
                            .get(index).work.image_urls.getLarge());
                }
            }
        });
        mCardView4 = view.findViewById(R.id.card_right);
        mCardView4.setOnTouchListener(this);
        mTagGroup = view.findViewById(R.id.tag_group);
        mTagGroup.setTags(FragmentPixivLeft.mPixivRankItem.response.get(0).works.get(index).work.tags);
        mTextView.setText(getString(R.string.string_author,
                FragmentPixivLeft.mPixivRankItem.response.get(0).works.get(index).work.user.getName()));
        mTextView2.setText(getString(R.string.string_full_size, FragmentPixivLeft.mPixivRankItem.response.get(0)
                .works.get(index).work.getWidth(), FragmentPixivLeft.mPixivRankItem.response.get(0)
                .works.get(index).work.getHeight()));
        mTextView3.setText(getString(R.string.string_create_time, FragmentPixivLeft.mPixivRankItem.response.get(0)
                .works.get(index).work.getCreated_time()));
        mTextView4.setText(getString(R.string.string_viewd,
                FragmentPixivLeft.mPixivRankItem.response.get(0).works.get(index).work.stats.getViews_count().substring(0,
                        FragmentPixivLeft.mPixivRankItem.response.get(0).works.get(index)
                                .work.stats.getViews_count().length() - 3)));
        if (FragmentPixivLeft.mPixivRankItem.response.get(0).works.get(index).work.stats.getScored_count().length() <= 3) {
            mTextView5.setText(FragmentPixivLeft.mPixivRankItem.response.get(0).works
                    .get(index).work.stats.getScored_count());
        } else {
            mTextView5.setText(getString(R.string.string_viewd,
                    FragmentPixivLeft.mPixivRankItem.response.get(0).works.get(index)
                            .work.stats.getScored_count().substring(0, FragmentPixivLeft.mPixivRankItem.response.get(0).works.get(index)
                            .work.stats.getScored_count().length() - 3)));
        }
        mTagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Label", tag);
                cm.setPrimaryClip(mClipData);
                TastyToast.makeText(mContext, tag + " 已复制到剪切板~", Toast.LENGTH_SHORT, TastyToast.SUCCESS).show();
            }
        });
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("提示信息");
        progressDialog.setMessage("正在下载...");
        progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.dismiss();
            }
        });
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(true);
    }

    public static FragmentPixivItem newInstance(int position) {
        Bundle args = new Bundle();
        args.putSerializable("index", position);
        FragmentPixivItem fragment = new FragmentPixivItem();
        fragment.setArguments(args);
        return fragment;
    }

    private class MyAsyncTask extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            InputStream inputStream = null;

            Bitmap bitmap = null;
            try {

                FileOutputStream outputStream = new FileOutputStream(
                        Environment.getExternalStorageDirectory().getPath() + "/Download/" +
                                FragmentPixivLeft.mPixivRankItem.response.get(0).works.get(index).
                                        work.getTitle() + ".jpeg");
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Referer", "https://www.pixiv.net/member_illust.php?mode=medium&illust_id=" +
                        FragmentPixivLeft.mPixivRankItem.response.get(0).works.get(index).work.getId());
                connection.connect();
                // 获取输入流
                inputStream = connection.getInputStream();
                // 获取文件流大小，用于更新进度
                long file_length = connection.getContentLength();
                int len;
                int total_length = 0;
                byte[] data = new byte[1024];
                while ((len = inputStream.read(data)) != -1) {
                    total_length += len;
                    int value = (int) ((total_length / (float) file_length) * 100);
                    // 调用update函数，更新进度
                    publishProgress(value);
                    outputStream.write(data, 0, len);
                }
                outputStream.close();
                progressDialog.setMessage("正在下载...");
                try {
                    MediaStore.Images.Media.insertImage(mContext.getContentResolver(),
                            Environment.getExternalStorageDirectory().getPath() + "/Download/",
                            FragmentPixivLeft.mPixivRankItem.response.get(0).works.get(index).work.getTitle() + ".jpeg",
                            null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath() + "/Download/",
                        FragmentPixivLeft.mPixivRankItem.response.get(0).works.get(index).work.getTitle() + ".jpeg"))));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return bitmap;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            progressDialog.dismiss();
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TastyToast.makeText(mContext, "下载完成~",
                            TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                }
            });
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (getActivity() != null) {
                ((ViewPagerActivity) getActivity()).changeTitle();
            }
        } else {
        }
    }
}