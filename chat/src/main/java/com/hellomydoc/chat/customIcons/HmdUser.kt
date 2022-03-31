package com.hellomydoc.chat.customIcons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

public val Icons.HmdUser: ImageVector
    get() {
        if (_name != null) {
            return _name!!
        }
        _name = materialIcon(name = "HmdUser") {
            materialPath {
                moveTo(20.95712f, 23.999842f)
                horizontalLineTo(3.0402138f)
                arcTo(3.0665934f,3.0665934f,0.0f, false,true,0.6282714f,22.849203f,)
                arcTo(2.7612674f,2.7612674f,0.0f, false,true,0.08295111f,20.423927f,)
                arcTo(12.133044f,12.133044f,0.0f, false,true,8.61608f,11.765467f,)
                arcTo(6.2865167f,6.2865167f,0.0f, false,true,5.5054884f,6.349597f,)
                arcTo(6.4291797f,6.4291797f,0.0f, false,true,11.998667f,4.1502406E-4f,)
                arcTo(6.4291797f,6.4291797f,0.0f, false,true,18.491844f,6.349597f,)
                arcToRelative(6.2851834f,6.2851834f,0.0f,false,true,-3.107926f,5.41587f)
                arcToRelative(12.133044f,12.133044f,0.0f,false,true,8.53313f,8.65846f)
                arcToRelative(2.7612674f,2.7612674f,0.0f,false,true,-0.54532f,2.425276f)
                arcToRelative(3.0665934f,3.0665934f,0.0f,false,true,-2.414609f,1.150639f)
                close()
                moveTo(11.998667f, 12.697445f)
                arcToRelative(10.778409f,10.778409f,0.0f,false,false,-10.533082f,8.059808f)
                arcToRelative(1.3933f,1.3933f,0.0f,false,false,0.2826599f,1.233304f)
                arcToRelative(1.6639603f,1.6639603f,0.0f,false,false,1.2906358f,0.615985f)
                horizontalLineTo(20.95712f)
                arcToRelative(1.6639603f,1.6639603f,0.0f,false,false,1.289302f,-0.615985f)
                arcToRelative(1.3933f,1.3933f,0.0f,false,false,0.28266f,-1.233304f)
                arcToRelative(10.778409f,10.778409f,0.0f,false,false,-10.530415f,-8.059808f)
                close()
                moveToRelative(0.0f, -11.302397f)
                arcToRelative(5.017214f,5.017214f,0.0f,false,false,-5.066546f,4.9545484f)
                arcToRelative(5.066546f,5.066546f,0.0f,false,false,10.133092f,0.0f)
                arcToRelative(5.017214f,5.017214f,0.0f,false,false,-5.066546f,-4.9545484f)
                close()
            }
        }
        return _name!!
    }

private var _name: ImageVector? = null