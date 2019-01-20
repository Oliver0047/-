package com.miao.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.miao.R;
import com.miao.baseInfo.CourseInfo;
import com.miao.baseInfo.GlobalInfo;
import com.miao.baseInfo.UserCourse;
import com.miao.common.Utility;
import com.miao.db.dao.CourseInfoDao;
import com.miao.db.dao.GlobalInfoDao;
import com.miao.db.dao.UserCourseDao;


/**
 * Created by miao on 2017/3/21.
 */

public class CourseDetailInfoActivity extends FragmentActivity{

    private static Context context;
    private ProgressDialog progressDialog;
    protected View backView;
    protected TextView titleView;

    protected TextView courseNameView;//课程名
    protected TextView courseTeacherView;//任课老师
    protected TextView courseTimeView;//上课时间
    protected TextView courseWeekView;//上课周数
    protected TextView courseSpaceView;//上课地点
    protected Button editButton;//编辑课程
    protected Button deleteButton;//删除课程
    CourseInfoDao cInfoDao;
    GlobalInfoDao gInfoDao;
    UserCourseDao uCourseDao;

    GlobalInfo gInfo;//需要isFirstUse和activeUserUid

    CourseInfo cInfo;

    private int uid,cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // 继承父类方法，绑定View
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        // 初始化context
        context = getApplicationContext();

        // 初始化Dao成员变量
        gInfoDao = new GlobalInfoDao(context);
        uCourseDao = new UserCourseDao(context);
        cInfoDao = new CourseInfoDao(context);
        // 初始化数据模型变量
        gInfo = gInfoDao.query();
        uid = gInfo.getActiveUserUid();
        cInfo = new CourseInfo();
        cInfo = (CourseInfo) getIntent().getSerializableExtra("courseInfo");//从Intent中取回Serializable的course_info内容
        cid = cInfo.getCid();

        // 初始化临时变量

        // 自定义函数
        initView();//初始化CourseActivity界面
        initListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void initView() {
        backView = findViewById(R.id.Btn_Course_Back);
        //设置标题栏
        titleView = (TextView) findViewById(R.id.Text_Course_Title);
        titleView.setTextSize(20);
        titleView.setPadding(15, 2, 15, 2);

        courseNameView = (TextView) findViewById(R.id.Detail_Course_Name);
        courseTeacherView = (TextView) findViewById(R.id.Detail_Course_Teacher);
        courseTimeView = (TextView) findViewById(R.id.Detail_Course_Time);
        courseWeekView = (TextView) findViewById(R.id.Detail_Course_Week);
        courseSpaceView = (TextView) findViewById(R.id.Detail_Course_Space);

        courseNameView.setText("课程名：" + cInfo.getCoursename());
        courseTeacherView.setText("任课教师：" + cInfo.getTeacher());
        courseTimeView.setText("时间：星期" + Utility.getDayStr(cInfo.getDay()) + " 第" + cInfo.getLessonfrom() + "节 - 第" + cInfo.getLessonto() + "节");
        if(cInfo.getWeektype()==1) {
            courseWeekView.setText("周数：第" +  cInfo.getWeekfrom() + "周 - 第" + cInfo.getWeekto() + "周");
        }else if(cInfo.getWeektype()==2) {
            courseWeekView.setText("周数：第" +  cInfo.getWeekfrom() + "节 - 第" + cInfo.getWeekto()+ "周 单周");
        }else if(cInfo.getWeektype()==3) {
            courseWeekView.setText("周数：第" +  cInfo.getWeekfrom() + "周 - 第" + cInfo.getWeekto()+ "周 双周");
        }
        courseSpaceView.setText("地点：" + cInfo.getPlace());

        deleteButton = (Button) findViewById(R.id.Btn_Delete);
        editButton =(Button)findViewById(R.id.Btn_Edit);
    }

    private void initListener() {
        backView.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                jumpToCourse();
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editCourse();
            }
        });
        deleteButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                deleteCourse();
            }
        });

    }

    private void editCourse() {
        Intent intent = new Intent();
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("courseInfo",cInfo );
        mBundle.putSerializable("uid",uid);
        intent.putExtras(mBundle);
        intent.setClass(CourseDetailInfoActivity.this, EditActivity.class);
        startActivity(intent);
        finish();
    }


    private void deleteCourse(){
        // 显示状态对话框
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.loading_tip));
        progressDialog.setCancelable(true);
        progressDialog.show();
        UserCourse ucourse= new UserCourse();
        ucourse.setUid(uid);
        ucourse.setCid(cid);
        uCourseDao.delete(ucourse);
        cInfoDao.delete(cid);
        Toast.makeText(CourseDetailInfoActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
        jumpToCourse();
    }


    private void jumpToCourse() {//返回课程界面
        Intent intent = new Intent();
        intent.setClass(this, CourseActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        finish();
    }




}
