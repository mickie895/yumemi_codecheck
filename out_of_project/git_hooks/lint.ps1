New-Item lint\reports -ItemType Directory -Force
echo '**start android linter**'
.\gradlew lint > lint/reports/lintresult.log
$studioLint = $LASTEXITCODE
echo '**start kotlin linter**'
.\gradlew ktlint > lint/reports/ktlintresult.log
$ktLint = $LASTEXITCODE

if ($studioLint -ne 0) {
    echo 'lint FAULT!', 'Check "lint/reports/lintresult.log"'
}

if ($ktLint -ne 0){
    echo 'lint FAULT!', 'Check "lint/reports/ktlintresult.log"'
}

if (($studioLint -ne 0) -or ($ktLint -ne 0)){
    exit 1
}

echo 'linter finished'
exit 0
