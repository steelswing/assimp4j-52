#
# Generated Makefile - do not edit!
#
# Edit the Makefile in the project folder instead (../Makefile). Each target
# has a -pre and a -post target defined where you can add customized code.
#
# This makefile implements configuration specific macros and targets.


# Environment
MKDIR=mkdir
CP=cp
GREP=grep
NM=nm
CCADMIN=CCadmin
RANLIB=ranlib
CC=gcc
CCC=g++
CXX=g++
FC=gfortran
AS=as

# Macros
CND_PLATFORM=MinGW_1-Windows
CND_DLIB_EXT=dll
CND_CONF=Debug
CND_DISTDIR=dist
CND_BUILDDIR=build

# Include project Makefile
include Makefile

# Object Directory
OBJECTDIR=${CND_BUILDDIR}/${CND_CONF}/${CND_PLATFORM}

# Object Files
OBJECTFILES= \
	${OBJECTDIR}/src/jassimp.o


# C Compiler Flags
CFLAGS=

# CC Compiler Flags
CCFLAGS=
CXXFLAGS=

# Fortran Compiler Flags
FFLAGS=

# Assembler Flags
ASFLAGS=

# Link Libraries and Options
LDLIBSOPTIONS=-Llib lib/libassimp.dll.a lib/libassimp-5.dll

# Build Targets
.build-conf: ${BUILD_SUBPROJECTS}
	"${MAKE}"  -f nbproject/Makefile-${CND_CONF}.mk ${CND_DISTDIR}/libjassimp.${CND_DLIB_EXT}
	${CP} lib/libassimp-5.dll ${CND_DISTDIR}

${CND_DISTDIR}/libjassimp.${CND_DLIB_EXT}: lib/libassimp.dll.a

${CND_DISTDIR}/libjassimp.${CND_DLIB_EXT}: lib/libassimp-5.dll

${CND_DISTDIR}/libjassimp.${CND_DLIB_EXT}: ${OBJECTFILES}
	${MKDIR} -p ${CND_DISTDIR}
	g++ -o ${CND_DISTDIR}/libjassimp.${CND_DLIB_EXT} ${OBJECTFILES} ${LDLIBSOPTIONS} -LC:/msys64/mingw64/lib -static --static -static-libgcc -static-libstdc++ -shared -s

${OBJECTDIR}/src/jassimp.o: src/jassimp.cpp
	${MKDIR} -p ${OBJECTDIR}/src
	${RM} "$@.d"
	$(COMPILE.cc) -g -I/C/Program\ Files/Java/jdk1.8.0_342/include -I/C/Program\ Files/Java/jdk1.8.0_342/include/win32 -Iinclude -std=c++11  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/src/jassimp.o src/jassimp.cpp

# Subprojects
.build-subprojects:

# Clean Targets
.clean-conf: ${CLEAN_SUBPROJECTS}
	${RM} -r ${CND_BUILDDIR}/${CND_CONF}
	${RM} -r ${CND_DISTDIR}/libassimp-5.dll
	${RM} ${CND_DISTDIR}/libjassimp.${CND_DLIB_EXT}

# Subprojects
.clean-subprojects:

# Enable dependency checking
.dep.inc: .depcheck-impl

include .dep.inc
