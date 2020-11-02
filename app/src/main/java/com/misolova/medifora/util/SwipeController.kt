package com.misolova.medifora.util

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE
import androidx.recyclerview.widget.RecyclerView

enum class ButtonState{
    GONE,
    LEFT_VISIBLE,
    RIGHT_VISIBLE
}

class SwipeController(): ItemTouchHelper.Callback() {
    companion object{
        const val BUTTON_WIDTH = 300F
    }

    private lateinit var buttonsActions: SwipeControllerActions
    var buttonInstance: RectF? = null
    private var swipeBack = false
    private var buttonShowedState = ButtonState.GONE
    private var currentItemViewHolder: RecyclerView.ViewHolder? = null

    constructor(buttonsActions: SwipeControllerActions) : this() {
        this.buttonsActions = buttonsActions
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0, ItemTouchHelper.LEFT )
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        var mydX = dX
        if(actionState == ACTION_STATE_SWIPE){
            if(buttonShowedState != ButtonState.GONE){
                if(buttonShowedState == ButtonState.LEFT_VISIBLE) mydX = Math.max(dX, BUTTON_WIDTH)
                if(buttonShowedState == ButtonState.RIGHT_VISIBLE) mydX = Math.min(dX, -BUTTON_WIDTH)
                super.onChildDraw(c, recyclerView, viewHolder, mydX, dY, actionState, isCurrentlyActive)
            } else{
                setTouchListener(c, recyclerView, viewHolder, mydX, dY, actionState, isCurrentlyActive)
            }
        }
        if(buttonShowedState == ButtonState.GONE){
            super.onChildDraw(c, recyclerView, viewHolder, mydX, dY, actionState, isCurrentlyActive)
        }
        currentItemViewHolder = viewHolder
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if(swipeBack){
            swipeBack = buttonShowedState != ButtonState.GONE
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    private fun setTouchListener(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean){
        recyclerView.setOnTouchListener { v: View, event: MotionEvent ->
            swipeBack = event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP
            if(swipeBack){
                if(dX < -BUTTON_WIDTH) buttonShowedState = ButtonState.RIGHT_VISIBLE
                else if (dX > BUTTON_WIDTH) buttonShowedState = ButtonState.LEFT_VISIBLE

                if(buttonShowedState != ButtonState.GONE){
                    setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    setItemsClickable(recyclerView, false)
                }
            }
            return@setOnTouchListener false
        }
    }

    private fun setTouchDownListener(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean){
        recyclerView.setOnTouchListener { v, event ->
            if(event.action == MotionEvent.ACTION_DOWN){
                setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
            return@setOnTouchListener false
        }
    }

    private fun setTouchUpListener(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean){
        recyclerView.setOnTouchListener { v, event ->
            if(event.action == MotionEvent.ACTION_UP){
                SwipeController().onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                recyclerView.setOnTouchListener { v, event ->
                    return@setOnTouchListener  false
                }
                setItemsClickable(recyclerView, true)
                swipeBack = false

                if(buttonsActions != null && buttonInstance != null && buttonInstance?.contains(event.getX(), event.getY())!!){
                    if(buttonShowedState == ButtonState.LEFT_VISIBLE){
                        buttonsActions.onLeftClicked(viewHolder.adapterPosition)
                    } else if(buttonShowedState == ButtonState.RIGHT_VISIBLE){
                        buttonsActions.onRightClicked(viewHolder.adapterPosition)
                    }
                }
                buttonShowedState = ButtonState.GONE
                currentItemViewHolder = null
            }
            return@setOnTouchListener false
        }
    }

    private fun setItemsClickable(recyclerView: RecyclerView, isClickable: Boolean){
        for (i in 0..recyclerView.childCount){
            recyclerView.getChildAt(i).isClickable = isClickable
        }
    }

    private fun drawButtons(c: Canvas, viewHolder: RecyclerView.ViewHolder){
        val buttonWidthWithoutPadding = BUTTON_WIDTH - 20
        val corners: Float = 16F
        val itemView = viewHolder.itemView
        val p = Paint()

        val leftButton: RectF = RectF(itemView.left.toFloat(), itemView.top.toFloat(), itemView.left + buttonWidthWithoutPadding, itemView.bottom.toFloat())
        p.color = Color.BLUE
        c.drawRoundRect(leftButton, corners, corners, p)
        drawText("EDIT", c, leftButton, p)

        val rightButton: RectF = RectF(itemView.right.toFloat() - buttonWidthWithoutPadding, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
        p.color = Color.RED
        c.drawRoundRect(rightButton, corners, corners, p)
        drawText("DELETE", c, rightButton, p)

        buttonInstance = null
        if(buttonShowedState == ButtonState.LEFT_VISIBLE){
            buttonInstance = leftButton
        } else if(buttonShowedState == ButtonState.RIGHT_VISIBLE){
            buttonInstance = rightButton
        }
    }

    private fun drawText(text: String, c: Canvas, button: RectF, p: Paint){
        val textSize = 60F
        p.color = Color.WHITE
        p.isAntiAlias = true
        p.textSize = textSize

        val textWidth = p.measureText(text)
        c.drawText(text, button.centerX()-(textWidth/2), button.centerY()+(textSize/2), p)
    }

    fun onDraw(c: Canvas){
        currentItemViewHolder?.let { drawButtons(c, it) }
    }

}