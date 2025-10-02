package com.easyestate.android.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.easyestate.android.ui.theme.EasyEstateTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUnitScreen(
    onBack: () -> Unit,
    onAddUnit: () -> Unit,
    modifier: Modifier = Modifier
) {
    var unitName by rememberSaveable { mutableStateOf("") }
    var property by rememberSaveable { mutableStateOf("") }
    var classification by rememberSaveable { mutableStateOf("") }
    var status by rememberSaveable { mutableStateOf("") }
    var rentAmount by rememberSaveable { mutableStateOf("") }
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
                        "Add Unit",
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
                    onClick = onAddUnit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Add Unit", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
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
                FormField(label = "Unit Name / Number") {
                    OutlinedTextField(
                        value = unitName,
                        onValueChange = { unitName = it },
                        placeholder = { Text("e.g. A101") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = textFieldColors,
                        singleLine = true
                    )
                }
            }
            item {
                FormField(label = "Property") {
                    UnitDropdown(
                        label = "Select a property",
                        items = listOf("Sunnyvale House", "Lakeside Apartments", "The Grand Residence"),
                        selectedItem = property,
                        onItemSelected = { property = it }
                    )
                }
            }
            item {
                FormField(label = "Classification") {
                    OutlinedTextField(
                        value = classification,
                        onValueChange = { classification = it },
                        placeholder = { Text("e.g. 3 Bedroom") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = textFieldColors,
                        singleLine = true
                    )
                }
            }
            item {
                FormField(label = "Status") {
                    UnitDropdown(
                        label = "Select status",
                        items = listOf("Occupied", "Vacant", "Under Maintenance"),
                        selectedItem = status,
                        onItemSelected = { status = it }
                    )
                }
            }
            item {
                FormField(label = "Rent Amount") {
                    OutlinedTextField(
                        value = rentAmount,
                        onValueChange = { rentAmount = it },
                        placeholder = { Text("e.g. 1500") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = textFieldColors,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }
            item {
                FormField(label = "Notes") {
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        placeholder = { Text("e.g. Balcony has a great view") },
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
private fun UnitDropdown(
    label: String,
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedItem.ifEmpty { label },
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
                unfocusedTextColor = if (selectedItem.isEmpty()) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurface
            )
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddUnitScreenPreview() {
    EasyEstateTheme {
        AddUnitScreen(onBack = {}, onAddUnit = {})
    }
}