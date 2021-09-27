package com.example.podlodkaandroidcrew.ui.session

import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.podlodkaandroidcrew.R
import com.example.podlodkaandroidcrew.data.model.Session

@ExperimentalCoilApi
@Composable
fun SessionImage(
    session: Session,
    modifier: Modifier = Modifier
) {
    Image(
        painter = rememberImagePainter(
            data = session.imageUrl,
            builder = {
                transformations(CircleCropTransformation())
            }
        ),
        contentDescription = null,
        modifier = modifier
    )
}