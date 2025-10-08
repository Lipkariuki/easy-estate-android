package com.easyestate.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.easyestate.android.ui.theme.EasyEstateTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPropertyScreen(
    onBack: () -> Unit,
    onAddProperty: (
        name: String,
        propertyType: String,
        address: String,
        city: String,
        location: String,
        notes: String
    ) -> Unit,
    isSubmitting: Boolean = false,
    modifier: Modifier = Modifier
) {
    var propertyName by rememberSaveable { mutableStateOf("") }
    var propertyType by rememberSaveable { mutableStateOf("") }
    var propertyTypeLabel by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    var city by rememberSaveable { mutableStateOf("") }
    var location by rememberSaveable { mutableStateOf("") }
    var notes by rememberSaveable { mutableStateOf("") }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
        focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Add Property",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Spacer(modifier = Modifier.size(48.dp)) // Balance the title
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Button(
                    onClick = { onAddProperty(propertyName, propertyType, address, city, location, notes) },
                    enabled = !isSubmitting,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        if (isSubmitting) "Saving…" else "Add Property",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                FormField(label = "Property Name") {
                    OutlinedTextField(
                        value = propertyName,
                        onValueChange = { propertyName = it },
                        placeholder = { Text("e.g. Sunnyvale House") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = textFieldColors,
                        singleLine = true
                    )
                }
            }
            item {
                FormField(label = "Property Type") {
                    PropertyTypeDropdown(
                        selectedLabel = propertyTypeLabel,
                        onTypeSelected = { option ->
                            propertyType = option.value
                            propertyTypeLabel = option.label
                        }
                    )
                }
            }
            item {
                FormField(label = "Address") {
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        placeholder = { Text("e.g. 106 Kilimani Road") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = textFieldColors,
                        singleLine = true
                    )
                }
            }
            item {
                FormField(label = "City / Town") {
                    OutlinedTextField(
                        value = city,
                        onValueChange = { city = it },
                        placeholder = { Text("e.g. Nairobi") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = textFieldColors,
                        singleLine = true
                    )
                }
            }
            item {
                FormField(label = "Location") {
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        placeholder = { Text("e.g. Kilimani") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = textFieldColors,
                        singleLine = true
                    )
                }
            }
            item {
                FormField(label = "Add Image") {
                    ImageDropzone()
                }
            }
            item {
                FormField(label = "Notes") {
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        placeholder = { Text("e.g. Near the park, has a new roof") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = textFieldColors
                    )
                }
            }
        }
    }
}

@Composable
private fun FormField(label: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PropertyTypeDropdown(
    selectedLabel: String,
    onTypeSelected: (PropertyTypeOption) -> Unit
) {
    val types = remember {
        listOf(
            PropertyTypeOption("🏢 Apartment", "apartment"),
            PropertyTypeOption("🏠 Residential House", "residential_house"),
            PropertyTypeOption("🏢 Office Space", "office_space"),
            PropertyTypeOption("🏭 Warehouse / Go-down", "warehouse"),
            PropertyTypeOption("🏘️ Airbnb / Short-term Rental", "airbnb"),
            PropertyTypeOption("🏬 Shop / Retail Unit", "retail_unit"),
            PropertyTypeOption("🌾 Plot / Land Parcel", "land_parcel"),
            PropertyTypeOption("🏫 Hostel / Institution", "hostel"),
            PropertyTypeOption("❓ Other (Specify)", "other")
        )
    }
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedLabel.ifEmpty { "Select property type" },
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedTextColor = if (selectedLabel.isEmpty()) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurface
            )
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            types.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.label) },
                    onClick = {
                        onTypeSelected(type)
                        expanded = false
                    }
                )
            }
        }
    }
}

private data class PropertyTypeOption(val label: String, val value: String)

@Composable
private fun ImageDropzone(modifier: Modifier = Modifier) {
    val borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(128.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
            .clickable { /* TODO: Handle image picking */ }
            .drawBehind {
                drawRoundRect(
                    color = borderColor,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = 2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    ),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx())
                )
            }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.CloudUpload,
                contentDescription = "Upload",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(32.dp)
            )
            Text(
                text = "Click to upload or drag and drop",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "SVG, PNG, JPG or GIF (MAX. 800x400px)",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddPropertyScreenPreview() {
    EasyEstateTheme {
        AddPropertyScreen(onBack = {}, onAddProperty = { _, _, _, _, _, _ -> })
    }
}
