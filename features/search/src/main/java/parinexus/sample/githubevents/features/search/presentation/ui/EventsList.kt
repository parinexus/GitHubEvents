package parinexus.sample.githubevents.features.search.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import parinexus.sample.githubevents.features.search.presentation.SearchScreenActions
import parinexus.sample.githubevents.features.searchapi.model.UserEvent
import parinexus.sample.githubevents.libraries.design.theme.PlaceHolderColor
import parinexus.sample.githubevents.libraries.design.R as DesignResource

@Composable
internal fun EventsList(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    lazyListState: LazyListState = rememberLazyListState(),
    items: LazyPagingItems<UserEvent>,
    actions: SearchScreenActions
) {

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.testTag("eventsList"),
            state = lazyListState,
            contentPadding = contentPadding
        ) {
            items(
                count = items.itemCount
            ) { index ->
                val item = items[index]
                if (item == null) {
                    EventPlaceHolder()
                } else {
                    Row(
                        modifier = Modifier
                            .clickable { actions.openEventDetail(item.id) }
                            .padding(vertical = 16.dp, horizontal = 16.dp)
                            .fillMaxWidth()
                            .height(48.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = item.userActor.avatarUrl.ifBlank { DesignResource.drawable.ic_github },
                            contentDescription = "userPhoto",
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(PlaceHolderColor),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth(),
                            text = item.userActor.login,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
