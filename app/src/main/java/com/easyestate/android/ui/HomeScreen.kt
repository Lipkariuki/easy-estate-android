package com.easyestate.android.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.easyestate.android.R
import com.easyestate.android.ui.theme.EasyEstateTheme

@Composable
fun HomeScreen(
    adminName: String,
    onAddProperty: () -> Unit,
    modifier: Modifier = Modifier
) {
    val filters = listOf("All", "Occupied", "Vacant", "Commercial")
    var selectedFilter by remember { mutableStateOf(filters.first()) }

    val properties = sampleProperties
    val rentStatuses = sampleRentStatus

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                HomeHeader(adminName = adminName)
            }
            item {
                FilterSection(
                    filters = filters,
                    selected = selectedFilter,
                    onSelect = { selectedFilter = it }
                )
            }
            item {
                SummaryCards()
            }
            item {
                PropertySection(properties)
            }
            item {
                RentStatusSection(rentStatuses, onAddProperty = onAddProperty)
            }
        }
    }
}

@Composable
private fun HomeHeader(adminName: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.3f)),
                        color = Color.Transparent
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(Color.White.copy(alpha = 0.25f), shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = adminName.firstOrNull()?.uppercase() ?: "A",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Text(
                            text = "Welcome back",
                            color = Color.White.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = adminName,
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Properties Overview",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )
        }
    }
}

@Composable
private fun FilterSection(filters: List<String>, selected: String, onSelect: (String) -> Unit) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(filters) { filter ->
            FilterChip(
                selected = filter == selected,
                onClick = { onSelect(filter) },
                label = { Text(filter) },
                shape = RoundedCornerShape(50),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = Color.White,
                    containerColor = MaterialTheme.colorScheme.surface,
                    labelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            )
        }
    }
}

@Composable
private fun SummaryCards() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MetricCard(
            title = "Income Due",
            value = "$12,500",
            subtitle = "Next 30 days",
            accent = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.weight(1f)
        )
        MetricCard(
            title = "Active Leases",
            value = "28/30",
            subtitle = "Units Occupied",
            accent = Color(0xFF8BC34A),
            modifier = Modifier.weight(1f)
        )
    }
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    subtitle: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(120.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
            Surface(
                color = accent.copy(alpha = 0.15f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = subtitle,
                    color = accent,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Composable
private fun PropertySection(properties: List<PropertyCard>) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Summary",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(12.dp))
        properties.forEach { property ->
            PropertyCardView(property)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun PropertyCardView(property: PropertyCard) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Image(
                    painter = painterResource(id = property.thumbnailRes),
                    contentDescription = property.title,
                    modifier = Modifier
                        .height(72.dp)
                        .width(96.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = property.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = property.address,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = property.price,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        StatusPill(label = property.statusLabel, background = property.statusColor)
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("${property.units} units", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "${property.occupied} occupied",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun StatusPill(label: String, background: Color) {
    Surface(
        color = background.copy(alpha = 0.2f),
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = label,
            color = background,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
private fun RentStatusSection(status: List<RentStatus>, onAddProperty: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Rent Status",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Surface(
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape,
                shadowElevation = 4.dp
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add property",
                    modifier = Modifier
                        .clickable(onClick = onAddProperty)
                        .size(40.dp)
                        .padding(8.dp),
                    tint = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        status.forEach { item ->
            RentStatusRow(item)
        }
    }
}

@Composable
private fun RentStatusRow(item: RentStatus) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = item.unit, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = item.address,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
        StatusPill(label = item.status, background = item.statusColor)
    }
}

private val sampleProperties = listOf(
    PropertyCard(
        title = "123 Main St.",
        address = "123 Main St.",
        price = "$1,800/mo",
        units = 3,
        occupied = 3,
        statusLabel = "Occupied",
        statusColor = Color(0xFF8BC34A)
    ),
    PropertyCard(
        title = "456 Oak Ave.",
        address = "456 Oak Ave.",
        price = "$1,800/mo",
        units = 3,
        occupied = 1,
        statusLabel = "Vacant",
        statusColor = Color(0xFF00B5C5)
    )
)

private val sampleRentStatus = listOf(
    RentStatus("Unit 101", "123 Main St.", "Paid", Color(0xFF8BC34A)),
    RentStatus("Unit 103", "456 Oak Ave.", "Paid", Color(0xFF8BC34A)),
    RentStatus("Unit 203", "456 Oak Ave.", "Due", Color(0xFFEFB867)),
    RentStatus("Unit 301", "789 Pine Rd.", "Paid", Color(0xFF8BC34A))
)

private data class PropertyCard(
    val title: String,
    val address: String,
    val price: String,
    val units: Int,
    val occupied: Int,
    val statusLabel: String,
    val statusColor: Color,
    val thumbnailRes: Int = R.drawable.placeholder_property
)

private data class RentStatus(
    val unit: String,
    val address: String,
    val status: String,
    val statusColor: Color
)

@Preview(showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    EasyEstateTheme {
        HomeScreen(adminName = "Easy Estate Admin", onAddProperty = {})
    }
}
