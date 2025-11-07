# Test All Gemini API Endpoints
# This script tests different Gemini models to find which ones work

$apiKey = "AIzaSyC0zdFP3Aj5V4jHrxcmVbJWMyveK5N-yns"

$models = @(
    "gemini-1.5-flash",
    "gemini-1.5-flash-latest",
    "gemini-1.5-pro",
    "gemini-1.5-pro-latest",
    "gemini-2.0-flash",
    "gemini-2.5-flash",
    "gemini-2.5-pro",
    "gemini-pro",
    "gemini-flash",
    "gemini-flash-latest"
)

Write-Host "`n================================================================" -ForegroundColor Cyan
Write-Host "GEMINI API ENDPOINT TESTER" -ForegroundColor Yellow
Write-Host "================================================================" -ForegroundColor Cyan
Write-Host "Testing API Key: $apiKey"
Write-Host "================================================================`n" -ForegroundColor Cyan

$workingModels = @()
$failedModels = @()

foreach ($model in $models) {
    Write-Host "Testing: $model ... " -NoNewline -ForegroundColor White
    
    $url = "https://generativelanguage.googleapis.com/v1beta/models/${model}:generateContent?key=$apiKey"
    $body = @{
        contents = @(
            @{
                parts = @(
                    @{
                        text = "Say hello"
                    }
                )
            }
        )
    } | ConvertTo-Json -Depth 5
    
    try {
        $response = Invoke-RestMethod -Uri $url -Method Post -Body $body -ContentType "application/json" -ErrorAction Stop
        Write-Host "WORKS!" -ForegroundColor Green
        $workingModels += $model
        Write-Host "  Response: $($response.candidates[0].content.parts[0].text)" -ForegroundColor Gray
    } catch {
        $statusCode = $_.Exception.Response.StatusCode.value__
        Write-Host "FAILED (Status: $statusCode)" -ForegroundColor Red
        $failedModels += $model
    }
    
    Start-Sleep -Milliseconds 500
}

Write-Host "`n================================================================" -ForegroundColor Cyan
Write-Host "SUMMARY" -ForegroundColor Yellow
Write-Host "================================================================" -ForegroundColor Cyan

if ($workingModels.Count -gt 0) {
    Write-Host "`nWORKING MODELS ($($workingModels.Count)):" -ForegroundColor Green
    foreach ($model in $workingModels) {
        Write-Host "  OK $model" -ForegroundColor Green
    }
    
    Write-Host "`nRECOMMENDED CONFIGURATION:" -ForegroundColor Yellow
    Write-Host "ai.api.url=https://generativelanguage.googleapis.com/v1beta/models/$($workingModels[0]):generateContent" -ForegroundColor Cyan
    Write-Host "ai.api.key=$apiKey" -ForegroundColor Cyan
    Write-Host "ai.model=$($workingModels[0])" -ForegroundColor Cyan
} else {
    Write-Host "`nNO WORKING MODELS FOUND!" -ForegroundColor Red
    Write-Host "Your API key may be invalid or restricted." -ForegroundColor Red
}

if ($failedModels.Count -gt 0) {
    Write-Host "`nFAILED MODELS ($($failedModels.Count)):" -ForegroundColor Red
    foreach ($model in $failedModels) {
        Write-Host "  X $model" -ForegroundColor Red
    }
}

Write-Host "`n================================================================`n" -ForegroundColor Cyan

