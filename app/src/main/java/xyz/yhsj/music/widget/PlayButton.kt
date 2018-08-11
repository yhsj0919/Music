package xyz.yhsj.music.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import xyz.yhsj.music.R


class PlayButton(context: Context, attrs: AttributeSet) : View(context, attrs) {

    companion object {
        const val PLAY_STATUS = 0
        const val PAUSE_STATUS = 1
    }

    // 画圆环的画笔
    lateinit var mRingPaint: Paint
    // 画方形的画笔
    lateinit var mRectPaint: Paint
    // 圆形颜色
    private var mCircleColor: Int = 0
    // 圆环颜色
    private var mRingColor: Int = 0
    // 半径
    private var mRadius: Float = 0.toFloat()
    // 圆环半径
    private var mRingRadius: Float = 0.toFloat()
    // 圆环宽度
    private var mStrokeWidth: Float = 0.toFloat()
    // 圆心x坐标
    private var mXCenter: Int = 0
    // 圆心y坐标
    private var mYCenter: Int = 0
    // 总进度
    private var mTotalProgress = 100
    // 当前进度
    private var mProgress = 0
    //大圆
    lateinit var mBigPatient: Paint
    //外圆颜色
    private var mBigCircleColor: Int = 0
    //方形颜色
    private var mRectColor: Int = 0

    private var status = PLAY_STATUS

    init {
        // 获取自定义的属性
        initAttrs(context, attrs)
        initVariable()
    }

    private fun initAttrs(context: Context, attrs: AttributeSet) {
        val typeArray = context.theme.obtainStyledAttributes(attrs, R.styleable.PlayButton, 0, 0)
        mRadius = typeArray.getDimension(R.styleable.PlayButton_radius, 300f)
        mStrokeWidth = typeArray.getDimension(R.styleable.PlayButton_strokeWidth, 20f)
        mCircleColor = typeArray.getColor(R.styleable.PlayButton_circleColor, Color.BLUE)
        mRingColor = typeArray.getColor(R.styleable.PlayButton_ringColor, Color.RED)
        mTotalProgress = typeArray.getInt(R.styleable.PlayButton_totalProgress, 100)
        mBigCircleColor = typeArray.getColor(R.styleable.PlayButton_bigCircleColor, Color.WHITE)
        mRectColor = typeArray.getColor(R.styleable.PlayButton_rectColor, Color.WHITE)
        typeArray.recycle()//注意这里要释放掉

        mRingRadius = mRadius + mStrokeWidth / 2
    }

    //初始化画笔
    private fun initVariable() {
        mRingPaint = Paint()
        mRingPaint.isAntiAlias = true
        mRingPaint.color = mRingColor
        mRingPaint.strokeCap = Paint.Cap.ROUND
        mRingPaint.style = Paint.Style.STROKE
        mRingPaint.strokeWidth = mStrokeWidth / 3 * 2

        mBigPatient = Paint()
        mBigPatient.color = mBigCircleColor
        mBigPatient.isAntiAlias = true
        mBigPatient.style = Paint.Style.STROKE
        mBigPatient.strokeWidth = mStrokeWidth / 2

        mRectPaint = Paint()
        mRectPaint.isAntiAlias = true
        mRectPaint.style = Paint.Style.FILL
        mRectPaint.color = mRectColor
        mRectPaint.strokeWidth = mStrokeWidth / 1.3F
    }

    override fun onDraw(canvas: Canvas) {
        //计算中心点
        mXCenter = width / 2
        mYCenter = height / 2

        //正在播放状态或者结束状态的时候 画圆环
        canvas.drawCircle(mXCenter.toFloat(), mYCenter.toFloat(), mRadius + mStrokeWidth / 2, mBigPatient)

        //判断是正在播放还是结束状态
        if (status == PAUSE_STATUS) {
            //正在播放状态，画出中间双竖
            canvas.drawLine(mXCenter - mRadius / 5, mYCenter.toFloat() - mRadius / 3 - mStrokeWidth / 4, mXCenter - mRadius / 5, mYCenter.toFloat() + mRadius / 3 + mStrokeWidth / 4, mRectPaint)
            canvas.drawLine(mXCenter + mRadius / 5, mYCenter.toFloat() - mRadius / 3 - mStrokeWidth / 4, mXCenter + mRadius / 5, mYCenter.toFloat() + mRadius / 3 + mStrokeWidth / 4, mRectPaint)
        } else {
            //结束播放状态，画三角形
            //竖线
            canvas.drawLine(mXCenter - mRadius / 6, mYCenter.toFloat() - mRadius / 3 - mStrokeWidth / 5, mXCenter - mRadius / 6, mYCenter.toFloat() + mRadius / 3 + mStrokeWidth / 5, mRectPaint)
            //上边
            canvas.drawLine(mXCenter + mRadius / 3, mYCenter.toFloat(), mXCenter - mRadius / 6, mYCenter - mRadius / 3, mRectPaint)
            //下边
            canvas.drawLine(mXCenter.toFloat() + mRadius / 3 + mStrokeWidth / 5, mYCenter.toFloat(), mXCenter - mRadius / 6, mYCenter + mRadius / 3, mRectPaint)
        }
        //根据进度画圆弧
        if (mProgress > 0) {
            val oval = RectF()
            oval.left = mXCenter - mRingRadius
            oval.top = mYCenter - mRingRadius
            oval.right = mRingRadius * 2 + (mXCenter - mRingRadius)
            oval.bottom = mRingRadius * 2 + (mYCenter - mRingRadius)
            canvas.drawArc(oval, -90f, mProgress.toFloat() / mTotalProgress * 360, false, mRingPaint) //
            if (mProgress == 100) {
                status = PLAY_STATUS
            }
        }
    }

    //设置进度的方法
    fun setProgress(progress: Int) {
        mProgress = progress
        if (status != PAUSE_STATUS) {
            status = PAUSE_STATUS
        }
        postInvalidate()


    }

    //设置总进度
    fun setTotalProgress(totalProgress: Int) {
        mTotalProgress = totalProgress
    }

    //设置当前的按钮的播放状态
    fun setStatus(mStatus: Int) {
        this.status = mStatus
        if (status == PAUSE_STATUS) {
            postInvalidate()
        }
    }

    //设置中间双竖的方法
    fun setRectColor(color: Int) {
        this.mRectColor = color
        mRectPaint.color = mRectColor
    }
}