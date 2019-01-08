package fragment;


import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.dimine_projectd.HeadImgTest;
import com.example.administrator.dimine_projectd.Login;
import com.example.administrator.dimine_projectd.ModifyPassword;
import com.example.administrator.dimine_projectd.MyApprovalActivity;
import com.example.administrator.dimine_projectd.MyNotification;
import com.example.administrator.dimine_projectd.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import utils.MyDialog;
import utils.ShowToast;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment implements View.OnClickListener {
    private View view;
    private ImageView mine_headview_img;
    private TextView mine_name_tv;
    private TextView mine_company_tv;
    private TextView mine_position_tv;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private FileInputStream fis;
    private File tempFile;
    private Bitmap bitmap;
    private Intent intent;
    private MyDialog dialog;

    private LinearLayout mine_approval;
    private LinearLayout mine_notification;
    private LinearLayout mine_about;
    private LinearLayout mine_modify_password;
    private LinearLayout mine_cancellation;

    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private static final int REQUEST_CODE = 1;
    /* 头像名称 */
    private static final String PHOTO_FILE_NAME = "headimg.jpg";
    private Uri mOutPutFileUri;

    public MineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = View.inflate(getActivity(), R.layout.fragment_mine, null);
            initView();
            setOnclick();
        }

        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    protected void initView() {
        mine_headview_img = (ImageView) view.findViewById(R.id.mine_headview_img);
        mine_name_tv = (TextView) view.findViewById(R.id.mine_name_tv);
        mine_company_tv = (TextView) view.findViewById(R.id.mine_company_tv);
        mine_position_tv = (TextView) view.findViewById(R.id.mine_position_tv);

        mine_approval = (LinearLayout) view.findViewById(R.id.mine_approval);
        mine_notification = (LinearLayout) view.findViewById(R.id.mine_notification);
        mine_about = (LinearLayout) view.findViewById(R.id.mine_about);

        mine_cancellation = (LinearLayout) view.findViewById(R.id.mine_cancellation);
        mine_modify_password = (LinearLayout) view.findViewById(R.id.mine_modify_password);


        sp = getActivity().getSharedPreferences("userInfo", getActivity().MODE_PRIVATE);
        editor = sp.edit();
        String name = sp.getString("name", null);
//        String img = sp.getString("img", null);
        String company = sp.getString("company", null);
        String position = sp.getString("position", null);
        mine_headview_img.setImageResource(R.drawable.head3);

        //设置用户头像
//        try {
//            fis = new FileInputStream(img);
//            Bitmap bm = BitmapFactory.decodeStream(fis);
//            //将图片显示到ImageView中
//            mine_headview_img.setImageBitmap(bm);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        //设置用户名字
        mine_name_tv.setText(name);
        //设置用户公司
        mine_company_tv.setText(company);
        //设置用户职位
        mine_position_tv.setText(position);
        editor.commit();

    }

    /**
     * 剪切图片
     */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        // 图片格式
        intent.putExtra("outputFormat", "PNG");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    //判断有没有SD卡
    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }


    //根据这个uri获得其在文件系统中SD卡的路径
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                crop(uri);
            }

        } else if (requestCode == PHOTO_REQUEST_CAMERA) {
            if (hasSdcard()) {
                tempFile = new File(Environment.getExternalStorageDirectory(),
                        PHOTO_FILE_NAME);
                crop(Uri.fromFile(tempFile));
            } else {
                ShowToast.showShort(getActivity(), "未找到存储卡，无法存储照片！");
            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            setImageToHeadView(data);
            dialog.dismiss();

//            if (intent != null) {
//                setImageToHeadView(intent);//设置图片框
//            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {

            bitmap = extras.getParcelable("data");
            this.mine_headview_img.setImageBitmap(bitmap);

            //新建文件夹 先选好路径 再调用mkdir函数 现在是根目录下面的Ask文件夹
            File nf = new File(Environment.getExternalStorageDirectory() + "/Ask");
            nf.mkdir();
            //在根目录下面的ASk文件夹下 创建okkk.jpg文件
            File f = new File(Environment.getExternalStorageDirectory() + "/Ask", "okkk.jpg");
            FileOutputStream out = null;
            //打开输出流 将图片数据填入文件中
            try {
                out = new FileOutputStream(f);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);

//                OkHttpUtils.postFile()
//                        .url("")
//                        .file(f)
//                        .build()
//                        .execute(new StringCallback() {
//                            @Override
//                            public void onError(Request request, Exception e) {
//                                    e.printStackTrace();
//                            }
//
//                            @Override
//                            public void onResponse(String response) {
//
//                            }
//                        });
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    private void saveFullImage() {
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File path1 = new File(path);
        if (!path1.exists()) {
            path1.mkdirs();
        }
        File file = new File(path1, System.currentTimeMillis() + ".png");
        mOutPutFileUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutPutFileUri);
        startActivityForResult(intent, REQUEST_CODE);
    }


    protected void setOnclick() {
        mine_approval.setOnClickListener(this);
        mine_notification.setOnClickListener(this);
        mine_about.setOnClickListener(this);
        mine_cancellation.setOnClickListener(this);
        mine_modify_password.setOnClickListener(this);
        mine_headview_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_cancellation:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.mine_cancellation_dialog_title);
                builder.setMessage(R.string.mine_cancellation_dialog_content);
                builder.setPositiveButton(R.string.mine_cancellation_dialog_btn2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent logoutIntent = new Intent(getActivity(), Login.class);
                        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(logoutIntent);
                        editor.clear();
                        editor.commit();
                    }
                });

                builder.setNegativeButton(R.string.mine_cancellation_dialog_btn1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                break;

            case R.id.mine_modify_password:
                intent = new Intent(getActivity(), ModifyPassword.class);
                startActivity(intent);
                break;

            case R.id.mine_headview_img:
                View dialog_view = View.inflate(getActivity(), R.layout.choose_headimg_dialog, null);
                dialog = new MyDialog(getActivity(), 0, 0, dialog_view, R.style.dialog);
                Window window = dialog.getWindow();
                window.setGravity(Gravity.BOTTOM);
                window.setWindowAnimations(R.style.mysharestyle);
                dialog.show();
                LinearLayout choose_headimg_xc = (LinearLayout) dialog_view.findViewById(R.id.choose_headimg_xc);
                LinearLayout choose_headimg_pz = (LinearLayout) dialog_view.findViewById(R.id.choose_headimg_pz);
                LinearLayout choose_headimg_cancel = (LinearLayout) dialog_view.findViewById(R.id.choose_headimg_cancel);

                choose_headimg_xc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 激活系统图库，选择一张图片
                        intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                    }
                });
                choose_headimg_pz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveFullImage();
                    }
                });

                choose_headimg_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.mine_approval:
                intent = new Intent(getActivity(), MyApprovalActivity.class);
                startActivity(intent);
                break;
            case R.id.mine_notification:
                intent = new Intent(getActivity(), MyNotification.class);
                startActivity(intent);
                break;
            case R.id.mine_about:
                ShowToast.showShort(getActivity(), R.string.mine_no_development);
                intent = new Intent(getActivity(), HeadImgTest.class);
                startActivity(intent);
                break;
        }
    }


}
