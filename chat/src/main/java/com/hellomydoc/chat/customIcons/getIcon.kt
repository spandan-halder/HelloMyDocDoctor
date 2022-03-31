package com.hellomydoc.chat.customIcons

import android.util.Log
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.graphics.vector.PathParser

fun Icon(pathData: String): ImageVector {
    val b = PathParser().parsePathString(pathData).toNodes()
    val ret = materialIcon("parsed_icon"){
        materialPath{
            b.forEach {
                when(it){
                    is PathNode.RelativeMoveTo->{
                        moveToRelative(it.dx, it.dy)
                    }
                    is PathNode.MoveTo->{
                        moveTo(it.x, it.y)
                    }
                    is PathNode.RelativeLineTo->{
                        lineToRelative(it.dx, it.dy)
                    }
                    is PathNode.LineTo->{
                        lineTo(it.x,it.y)
                    }
                    is PathNode.RelativeHorizontalTo->{
                        horizontalLineToRelative(it.dx)
                    }
                    is PathNode.HorizontalTo->{
                        horizontalLineTo(it.x)
                    }
                    is PathNode.RelativeVerticalTo->{
                        verticalLineToRelative(it.dy)
                    }
                    is PathNode.VerticalTo->{
                        verticalLineTo(it.y)
                    }
                    is PathNode.RelativeCurveTo->{
                        curveToRelative(
                            it.dx1,
                            it.dy1,
                            it.dx2,
                            it.dy2,
                            it.dx3,
                            it.dy3,
                        )
                    }
                    is PathNode.CurveTo->{
                        curveTo(
                            it.x1,
                            it.y1,
                            it.x2,
                            it.y2,
                            it.x3,
                            it.y3,
                        )
                    }
                    is PathNode.RelativeReflectiveCurveTo->{
                        reflectiveCurveToRelative(
                            it.dx1,
                            it.dy1,
                            it.dx2,
                            it.dy2,
                        )
                    }
                    is PathNode.ReflectiveCurveTo->{
                        reflectiveCurveTo(
                            it.x1,
                            it.y1,
                            it.x2,
                            it.y2,
                        )
                    }
                    is PathNode.RelativeQuadTo->{
                        quadToRelative(
                            it.dx1,
                            it.dy1,
                            it.dx2,
                            it.dy2,
                        )
                    }
                    is PathNode.QuadTo->{
                        quadTo(
                            it.x1,
                            it.y1,
                            it.x2,
                            it.y2,
                        )
                    }
                    is PathNode.RelativeReflectiveQuadTo->{
                        reflectiveQuadToRelative(
                            it.dx,
                            it.dy
                        )
                    }
                    is PathNode.ReflectiveQuadTo->{
                        reflectiveQuadTo(
                            it.x,
                            it.y
                        )
                    }
                    is PathNode.RelativeArcTo->{
                        arcToRelative(
                            it.horizontalEllipseRadius,
                            it.verticalEllipseRadius,
                            it.theta,
                            it.isMoreThanHalf,
                            it.isPositiveArc,
                            it.arcStartDx,
                            it.arcStartDy
                        )
                    }
                    is PathNode.ArcTo->{
                        arcTo(
                            it.horizontalEllipseRadius,
                            it.verticalEllipseRadius,
                            it.theta,
                            it.isMoreThanHalf,
                            it.isPositiveArc,
                            it.arcStartX,
                            it.arcStartY,
                        )
                    }
                    is PathNode.Close->{
                        close()
                    }
                    //else->nothing
                }
            }
        }
    }
    return ret
}

fun ComposeIcon(pathData: String): ImageVector {
    val b = PathParser().parsePathString(pathData).toNodes()
    val s = mutableListOf<String>()
    fun record(msg: String){
        s.add(msg)
    }
    val ret = materialIcon("parsed_icon"){
        materialPath{
            b.forEach {
                when(it){
                    is PathNode.RelativeMoveTo->{
                        moveToRelative(it.dx, it.dy)
                        record("moveToRelative(${it.dx}f, ${it.dy}f)")
                    }
                    is PathNode.MoveTo->{
                        moveTo(it.x, it.y)
                        record("moveTo(${it.x}f, ${it.y}f)")
                    }
                    is PathNode.RelativeLineTo->{
                        lineToRelative(it.dx, it.dy)
                        record("lineToRelative(${it.dx}f, ${it.dy}f)")
                    }
                    is PathNode.LineTo->{
                        lineTo(it.x,it.y)
                        record("lineTo(${it.x}f,${it.y}f)")
                    }
                    is PathNode.RelativeHorizontalTo->{
                        horizontalLineToRelative(it.dx)
                        record("horizontalLineToRelative(${it.dx}f)")
                    }
                    is PathNode.HorizontalTo->{
                        horizontalLineTo(it.x)
                        record("horizontalLineTo(${it.x}f)")
                    }
                    is PathNode.RelativeVerticalTo->{
                        verticalLineToRelative(it.dy)
                        record("verticalLineToRelative(${it.dy}f)")
                    }
                    is PathNode.VerticalTo->{
                        verticalLineTo(it.y)
                        record("verticalLineTo(${it.y}f)")
                    }
                    is PathNode.RelativeCurveTo->{
                        curveToRelative(
                            it.dx1,
                            it.dy1,
                            it.dx2,
                            it.dy2,
                            it.dx3,
                            it.dy3,
                        )
                        record("curveToRelative(${it.dx1}f,${it.dy1}f,${it.dx2}f,${it.dy2}f,${it.dx3}f,${it.dy3}f,)")
                    }
                    is PathNode.CurveTo->{
                        curveTo(
                            it.x1,
                            it.y1,
                            it.x2,
                            it.y2,
                            it.x3,
                            it.y3,
                        )
                        record("curveTo(${it.x1}f,${it.y1}f,${it.x2}f,${it.y2}f,${it.x3}f,${it.y3}f,)")
                    }
                    is PathNode.RelativeReflectiveCurveTo->{
                        reflectiveCurveToRelative(
                            it.dx1,
                            it.dy1,
                            it.dx2,
                            it.dy2,
                        )
                        record("reflectiveCurveToRelative(${it.dx1}f,${it.dy1}f,${it.dx2}f,${it.dy2}f,)")
                    }
                    is PathNode.ReflectiveCurveTo->{
                        reflectiveCurveTo(
                            it.x1,
                            it.y1,
                            it.x2,
                            it.y2,
                        )
                        record("reflectiveCurveTo(${it.x1}f,${it.y1}f,${it.x2}f,${it.y2}f,)")
                    }
                    is PathNode.RelativeQuadTo->{
                        quadToRelative(
                            it.dx1,
                            it.dy1,
                            it.dx2,
                            it.dy2,
                        )
                        record("quadToRelative(${it.dx1}f,${it.dy1}f,${it.dx2}f,${it.dy2}f,)")
                    }
                    is PathNode.QuadTo->{
                        quadTo(
                            it.x1,
                            it.y1,
                            it.x2,
                            it.y2,
                        )
                        record("quadTo(${it.x1}f,${it.y1}f,${it.x2}f,${it.y2}f,)")
                    }
                    is PathNode.RelativeReflectiveQuadTo->{
                        reflectiveQuadToRelative(
                            it.dx,
                            it.dy
                        )
                        record("reflectiveQuadToRelative(${it.dx}f,${it.dy}f)")
                    }
                    is PathNode.ReflectiveQuadTo->{
                        reflectiveQuadTo(
                            it.x,
                            it.y
                        )
                        record("reflectiveQuadTo(${it.x}f,${it.y}f)")
                    }
                    is PathNode.RelativeArcTo->{
                        arcToRelative(
                            it.horizontalEllipseRadius,
                            it.verticalEllipseRadius,
                            it.theta,
                            it.isMoreThanHalf,
                            it.isPositiveArc,
                            it.arcStartDx,
                            it.arcStartDy
                        )
                        record("arcToRelative(${it.horizontalEllipseRadius}f,${it.verticalEllipseRadius}f,${it.theta}f,${it.isMoreThanHalf},${it.isPositiveArc},${it.arcStartDx}f,${it.arcStartDy}f)")
                    }
                    is PathNode.ArcTo->{
                        arcTo(
                            it.horizontalEllipseRadius,
                            it.verticalEllipseRadius,
                            it.theta,
                            it.isMoreThanHalf,
                            it.isPositiveArc,
                            it.arcStartX,
                            it.arcStartY,
                        )
                        record("arcTo(${it.horizontalEllipseRadius}f,${it.verticalEllipseRadius}f,${it.theta}f, ${it.isMoreThanHalf},${it.isPositiveArc},${it.arcStartX}f,${it.arcStartY}f,)")
                    }
                    is PathNode.Close->{
                        close()
                        record("close()")
                    }
                    //else->nothing
                }
            }
        }
    }
    Log.d("parsed_icon",s.joinToString("\n"))
    return ret
}
/*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

public val Icons.name: ImageVector
    get() {
        if (_name != null) {
            return _name!!
        }
        _name = materialIcon(name = "name") {
            materialPath {
                //commands
            }
        }
        return _name!!
    }

private var _name: ImageVector? = null
* */
