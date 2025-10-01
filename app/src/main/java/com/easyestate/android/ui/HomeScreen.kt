package com.easyestate.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.easyestate.android.ui.theme.EasyEstateTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    adminName: String,
    modifier: Modifier = Modifier,
) {
    var searchQuery by remember { mutableStateOf("") }
    var showOnlyFavorites by remember { mutableStateOf(false) }
    var highlightedPropertyId by remember { mutableStateOf<Int?>(null) }

    val properties = remember { sampleProperties() }
    val filteredProperties = remember(searchQuery, showOnlyFavorites, highlightedPropertyId) {
        properties.filter { property ->
            val matchesQuery = property.title.contains(searchQuery, ignoreCase = true) ||
                property.location.contains(searchQuery, ignoreCase = true)
            val matchesFavorites = !showOnlyFavorites || property.isFavorite
            matchesQuery && matchesFavorites
        }.map { property ->
            if (property.id == highlightedPropertyId) {
                property.copy(isHighlighted = true)
            } else {
                property.copy(isHighlighted = false)
            }
        }
    }

    Surface(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Welcome back",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                        Text(
                            text = adminName,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                actions = {
                    val favoriteIcon = if (showOnlyFavorites) {
                        Icons.Filled.Favorite
                    } else {
                        Icons.Outlined.FavoriteBorder
                    }
                    val iconTint = if (showOnlyFavorites) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    }
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clip(CircleShape)
                            .clickable { showOnlyFavorites = !showOnlyFavorites }
                            .background(
                                color = if (showOnlyFavorites) {
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                                } else {
                                    Color.Transparent
                                }
                            )
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = favoriteIcon,
                            contentDescription = null,
                            tint = iconTint
                        )
                    }
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Discover premium properties",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search by city or property") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredProperties) { property ->
                    PropertyCard(
                        property = property,
                        onClick = { highlightedPropertyId = property.id }
                    )
                }
            }
        }
    }
}

@Composable
private fun PropertyCard(
    property: Property,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (property.isHighlighted) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = property.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = property.location,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = property.price,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = property.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

data class Property(
    val id: Int,
    val title: String,
    val location: String,
    val price: String,
    val description: String,
    val isFavorite: Boolean,
    val isHighlighted: Boolean = false
)

private fun sampleProperties(): List<Property> = listOf(
    Property(
        id = 1,
        title = "Modern Family Home",
        location = "Lagos, Nigeria",
        price = "₦85,000,000",
        description = "A spacious 4-bedroom home with a private garden and smart security system.",
        isFavorite = true
    ),
    Property(
        id = 2,
        title = "Luxury Waterfront Apartment",
        location = "Abuja, Nigeria",
        price = "₦120,000,000",
        description = "Premium waterfront living with panoramic views, concierge, and pool access.",
        isFavorite = false
    ),
    Property(
        id = 3,
        title = "Urban Smart Condo",
        location = "Port Harcourt, Nigeria",
        price = "₦68,000,000",
        description = "Smart-enabled condo featuring co-working space, gym, and rooftop lounge.",
        isFavorite = true
    )
)

@Preview
@Composable
private fun HomeScreenPreview() {
    EasyEstateTheme {
        HomeScreen(adminName = "Easy Estate Admin")
    }
}
